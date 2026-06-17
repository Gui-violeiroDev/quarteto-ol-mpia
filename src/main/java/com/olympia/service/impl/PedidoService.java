package com.olympia.service.impl;

import com.olympia.dto.request.PedidoRequest;
import com.olympia.dto.response.PartituraResponse;
import com.olympia.dto.response.PedidoResponse;
import com.olympia.entity.*;
import com.olympia.enums.StatusPedido;
import com.olympia.exception.RecursoNaoEncontradoException;
import com.olympia.exception.RegraDeNegocioException;
import com.olympia.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PartituraRepository partituraRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoOperacaoRepository historicoRepository;
    private final EmailService emailService;
    private final AgendaService agendaService;

    public static final double ADICIONAL_FORA_SP = 800.0;
    public static final double CUSTO_PARTITURA_NOVA = 150.0;

    public PedidoService(PedidoRepository pedidoRepository, PartituraRepository partituraRepository,
            UsuarioRepository usuarioRepository, HistoricoOperacaoRepository historicoRepository,
            EmailService emailService, AgendaService agendaService) {
        this.pedidoRepository = pedidoRepository;
        this.partituraRepository = partituraRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.emailService = emailService;
        this.agendaService = agendaService;
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest req, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        boolean disponivel = agendaService.verificarDisponibilidade(req.getTipoFormacao(), req.getDataEvento(), req.getHoraEvento());
        double valorBase = req.getTipoFormacao().getValorBase();
        boolean foraSp = calcularForaSP(req.getCidadeEvento());
        double adicionalDeslocamento = foraSp ? ADICIONAL_FORA_SP : 0.0;
        int qtdNovas = req.getQtdPartiurasNovas() != null ? req.getQtdPartiurasNovas() : 0;
        double adicionalPartituras = qtdNovas * CUSTO_PARTITURA_NOVA;
        double valorTotal = valorBase + adicionalDeslocamento + adicionalPartituras;

        List<Partitura> partituras = new ArrayList<>();
        if (req.getPartituraIds() != null) partituras = partituraRepository.findAllById(req.getPartituraIds());

        Pedido pedido = Pedido.builder()
                .nomeCliente(req.getNomeCliente()).emailCliente(req.getEmailCliente())
                .telefoneCliente(req.getTelefoneCliente()).tipoEvento(req.getTipoEvento())
                .dataEvento(req.getDataEvento()).horaEvento(req.getHoraEvento())
                .enderecoEvento(req.getEnderecoEvento()).cidadeEvento(req.getCidadeEvento())
                .estadoEvento(req.getEstadoEvento()).cepEvento(req.getCepEvento())
                .foraSp(foraSp).tipoFormacao(req.getTipoFormacao()).partituras(partituras)
                .qtdPartiurasNovas(qtdNovas).valorBase(valorBase)
                .adicionalDeslocamento(adicionalDeslocamento).adicionalPartituras(adicionalPartituras)
                .valorTotal(valorTotal).status(StatusPedido.PENDENTE)
                .observacoes(req.getObservacoes()).usuario(usuario).build();

        Pedido salvo = pedidoRepository.save(pedido);
        registrarHistorico("CRIAR_PEDIDO", "Pedido criado para " + req.getNomeCliente(), salvo.getId(), usuario);
        if (!disponivel) emailService.notificarAdminIndisponibilidade(salvo);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAllByOrderByCriadoEmDesc().stream().map(this::toResponse).toList();
    }

    // FIX: busca por usuarioId OU emailCliente numa única query, sem risco de perder pedidos antigos
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null) {
            return pedidoRepository.findByUsuarioIdOrEmailCliente(usuario.getId(), email)
                    .stream().map(this::toResponse).toList();
        }
        return pedidoRepository.findByEmailClienteOrderByCriadoEmDesc(email)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) { return toResponse(buscarEntidade(id)); }

    @Transactional
    public PedidoResponse atualizarStatus(Long id, StatusPedido novoStatus, String emailUsuario) {
        Pedido pedido = buscarEntidade(id);
        // Regra de negócio: pedido cancelado não pode ser reativado
        if (pedido.getStatus() == StatusPedido.CANCELADO && novoStatus != StatusPedido.CANCELADO) {
            throw new RegraDeNegocioException("Pedido cancelado não pode ser reativado");
        }
        pedido.setStatus(novoStatus);
        Pedido atualizado = pedidoRepository.save(pedido);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
        registrarHistorico("ATUALIZAR_STATUS", "Status alterado para " + novoStatus, id, usuario);
        return toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id, String emailUsuario) {
        Pedido pedido = buscarEntidade(id);
        // Regra de negócio: só pedidos PENDENTES podem ser cancelados pelo cliente
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new RegraDeNegocioException("Apenas pedidos PENDENTES podem ser cancelados");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
        registrarHistorico("CANCELAR_PEDIDO", "Pedido cancelado: " + id, id, usuario);
    }

    // Método utilitário público para facilitar testes unitários
    public boolean calcularForaSP(String cidade) {
        if (cidade == null) return false;
        String normalizado = cidade.trim();
        return !normalizado.equalsIgnoreCase("São Paulo")
                && !normalizado.equalsIgnoreCase("Sao Paulo")
                && !normalizado.equalsIgnoreCase("SP");
    }

    public double calcularValorTotal(double valorBase, boolean foraSp, int qtdNovas) {
        double adicionalDeslocamento = foraSp ? ADICIONAL_FORA_SP : 0.0;
        double adicionalPartituras = qtdNovas * CUSTO_PARTITURA_NOVA;
        return valorBase + adicionalDeslocamento + adicionalPartituras;
    }

    private Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));
    }

    private void registrarHistorico(String operacao, String descricao, Long registroId, Usuario usuario) {
        HistoricoOperacao h = HistoricoOperacao.builder()
                .operacao(operacao).descricao(descricao)
                .tabelaAfetada("pedidos").registroId(registroId).usuario(usuario).build();
        historicoRepository.save(h);
    }

    public PedidoResponse toResponse(Pedido p) {
        PedidoResponse r = new PedidoResponse();
        r.setId(p.getId()); r.setNomeCliente(p.getNomeCliente()); r.setEmailCliente(p.getEmailCliente());
        r.setTelefoneCliente(p.getTelefoneCliente()); r.setTipoEvento(p.getTipoEvento());
        r.setDataEvento(p.getDataEvento()); r.setHoraEvento(p.getHoraEvento());
        r.setEnderecoEvento(p.getEnderecoEvento()); r.setCidadeEvento(p.getCidadeEvento());
        r.setEstadoEvento(p.getEstadoEvento()); r.setCepEvento(p.getCepEvento());
        r.setForaSp(p.getForaSp()); r.setTipoFormacao(p.getTipoFormacao());
        r.setTipoFormacaoDescricao(p.getTipoFormacao().getDescricao());
        r.setPartituras(p.getPartituras().stream().map(pt -> {
            PartituraResponse pr = new PartituraResponse();
            pr.setId(pt.getId()); pr.setNomeMusica(pt.getNomeMusica()); pr.setNomeCompositor(pt.getNomeCompositor());
            return pr;
        }).toList());
        r.setQtdPartiurasNovas(p.getQtdPartiurasNovas()); r.setValorBase(p.getValorBase());
        r.setAdicionalDeslocamento(p.getAdicionalDeslocamento()); r.setAdicionalPartituras(p.getAdicionalPartituras());
        r.setValorTotal(p.getValorTotal()); r.setStatus(p.getStatus()); r.setObservacoes(p.getObservacoes());
        r.setCriadoEm(p.getCriadoEm()); r.setAtualizadoEm(p.getAtualizadoEm());
        return r;
    }
}

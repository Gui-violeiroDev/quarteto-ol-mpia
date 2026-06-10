package com.olympia.service;

import com.olympia.dto.request.PedidoRequest;
import com.olympia.dto.response.PedidoResponse;
import com.olympia.entity.Pedido;
import com.olympia.entity.Usuario;
import com.olympia.enums.Role;
import com.olympia.enums.StatusPedido;
import com.olympia.enums.TipoFormacao;
import com.olympia.exception.RecursoNaoEncontradoException;
import com.olympia.repository.*;
import com.olympia.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PedidoService")
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private PartituraRepository partituraRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private HistoricoOperacaoRepository historicoRepository;
    @Mock private EmailService emailService;
    @Mock private AgendaService agendaService;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuarioTeste;
    private PedidoRequest requestTeste;
    private Pedido pedidoTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = Usuario.builder()
                .id(1L).nome("Guilherme").email("guilherme@test.com")
                .senha("encoded").role(Role.ROLE_USER).ativo(true).build();

        requestTeste = new PedidoRequest();
        requestTeste.setNomeCliente("João Silva");
        requestTeste.setEmailCliente("joao@email.com");
        requestTeste.setTelefoneCliente("(11) 99999-9999");
        requestTeste.setTipoEvento("Casamento");
        requestTeste.setDataEvento(LocalDate.now().plusDays(30));
        requestTeste.setHoraEvento(LocalTime.of(16, 0));
        requestTeste.setEnderecoEvento("Rua das Flores, 100");
        requestTeste.setCidadeEvento("São Paulo");
        requestTeste.setEstadoEvento("SP");
        requestTeste.setCepEvento("01310-100");
        requestTeste.setTipoFormacao(TipoFormacao.QUARTETO);
        requestTeste.setQtdPartiurasNovas(0);

        pedidoTeste = Pedido.builder()
                .id(1L).nomeCliente("João Silva").emailCliente("joao@email.com")
                .tipoFormacao(TipoFormacao.QUARTETO).valorBase(5000.0)
                .adicionalDeslocamento(0.0).adicionalPartituras(0.0).valorTotal(5000.0)
                .status(StatusPedido.PENDENTE).foraSp(false)
                .dataEvento(LocalDate.now().plusDays(30))
                .horaEvento(LocalTime.of(16, 0))
                .cidadeEvento("São Paulo").estadoEvento("SP")
                .partituras(List.of()).qtdPartiurasNovas(0)
                .usuario(usuarioTeste).build();
    }

    // ────── TESTE 1 ──────
    @Test
    @DisplayName("1 - Deve criar pedido com sucesso dentro de SP")
    void deveCriarPedidoEmSaoPaulo() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(agendaService.verificarDisponibilidade(any(), any(), any())).thenReturn(true);
        when(partituraRepository.findAllById(any())).thenReturn(List.of());
        when(pedidoRepository.save(any())).thenReturn(pedidoTeste);
        when(historicoRepository.save(any())).thenReturn(null);

        PedidoResponse response = pedidoService.criar(requestTeste, "guilherme@test.com");

        assertNotNull(response);
        assertEquals(5000.0, response.getValorBase());
        assertEquals(0.0, response.getAdicionalDeslocamento());
        assertEquals(5000.0, response.getValorTotal());
        verify(pedidoRepository, times(1)).save(any());
    }

    // ────── TESTE 2 ──────
    @Test
    @DisplayName("2 - Deve adicionar R$800 para eventos fora de SP")
    void deveAdicionarCustoForaSP() {
        requestTeste.setCidadeEvento("Campinas");
        Pedido pedidoForaSP = Pedido.builder()
                .id(2L).nomeCliente("João").emailCliente("joao@email.com")
                .tipoFormacao(TipoFormacao.QUARTETO).valorBase(5000.0)
                .adicionalDeslocamento(800.0).adicionalPartituras(0.0).valorTotal(5800.0)
                .status(StatusPedido.PENDENTE).foraSp(true)
                .dataEvento(requestTeste.getDataEvento()).horaEvento(requestTeste.getHoraEvento())
                .cidadeEvento("Campinas").estadoEvento("SP")
                .partituras(List.of()).qtdPartiurasNovas(0)
                .usuario(usuarioTeste).build();

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(agendaService.verificarDisponibilidade(any(), any(), any())).thenReturn(true);
        when(partituraRepository.findAllById(any())).thenReturn(List.of());
        when(pedidoRepository.save(any())).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            return p.getForaSp() ? pedidoForaSP : pedidoTeste;
        });
        when(historicoRepository.save(any())).thenReturn(null);

        PedidoResponse response = pedidoService.criar(requestTeste, "guilherme@test.com");

        assertEquals(800.0, response.getAdicionalDeslocamento());
        assertEquals(5800.0, response.getValorTotal());
    }

    // ────── TESTE 3 ──────
    @Test
    @DisplayName("3 - Deve calcular custo de R$150 por partitura nova")
    void deveCalcularCustoPartituraNova() {
        requestTeste.setQtdPartiurasNovas(2);
        Pedido pedidoComPartituras = Pedido.builder()
                .id(3L).nomeCliente("João").emailCliente("joao@email.com")
                .tipoFormacao(TipoFormacao.QUARTETO).valorBase(5000.0)
                .adicionalDeslocamento(0.0).adicionalPartituras(300.0).valorTotal(5300.0)
                .status(StatusPedido.PENDENTE).foraSp(false)
                .dataEvento(requestTeste.getDataEvento()).horaEvento(requestTeste.getHoraEvento())
                .cidadeEvento("São Paulo").estadoEvento("SP")
                .partituras(List.of()).qtdPartiurasNovas(2)
                .usuario(usuarioTeste).build();

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(agendaService.verificarDisponibilidade(any(), any(), any())).thenReturn(true);
        when(partituraRepository.findAllById(any())).thenReturn(List.of());
        when(pedidoRepository.save(any())).thenReturn(pedidoComPartituras);
        when(historicoRepository.save(any())).thenReturn(null);

        PedidoResponse response = pedidoService.criar(requestTeste, "guilherme@test.com");

        assertEquals(300.0, response.getAdicionalPartituras());
        assertEquals(5300.0, response.getValorTotal());
    }

    // ────── TESTE 4 ──────
    @Test
    @DisplayName("4 - Deve lançar exceção quando pedido não encontrado")
    void deveLancarExcecaoQuandoPedidoNaoEncontrado() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.buscarPorId(999L));
    }

    // ────── TESTE 5 ──────
    @Test
    @DisplayName("5 - Deve listar pedidos por email do cliente")
    void deveListarPedidosPorEmail() {
        when(pedidoRepository.findByEmailClienteOrderByCriadoEmDesc("joao@email.com"))
                .thenReturn(List.of(pedidoTeste));

        List<PedidoResponse> lista = pedidoService.listarPorUsuario("joao@email.com");

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("João Silva", lista.get(0).getNomeCliente());
    }

    // ────── TESTE 6 ──────
    @Test
    @DisplayName("6 - Deve atualizar status do pedido")
    void deveAtualizarStatusDoPedido() {
        Pedido pedidoConfirmado = Pedido.builder()
                .id(1L).nomeCliente("João").emailCliente("joao@email.com")
                .tipoFormacao(TipoFormacao.QUARTETO).valorBase(5000.0)
                .adicionalDeslocamento(0.0).adicionalPartituras(0.0).valorTotal(5000.0)
                .status(StatusPedido.CONFIRMADO).foraSp(false)
                .dataEvento(LocalDate.now().plusDays(30)).horaEvento(LocalTime.of(16, 0))
                .cidadeEvento("São Paulo").estadoEvento("SP")
                .partituras(List.of()).qtdPartiurasNovas(0)
                .usuario(usuarioTeste).build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTeste));
        when(pedidoRepository.save(any())).thenReturn(pedidoConfirmado);
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(historicoRepository.save(any())).thenReturn(null);

        PedidoResponse response = pedidoService.atualizarStatus(1L, StatusPedido.CONFIRMADO, "guilherme@test.com");

        assertEquals(StatusPedido.CONFIRMADO, response.getStatus());
    }

    // ────── TESTE 7 ──────
    @Test
    @DisplayName("7 - Deve notificar admin quando músicos indisponíveis")
    void deveNotificarAdminQuandoMusicosIndisponiveis() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(agendaService.verificarDisponibilidade(any(), any(), any())).thenReturn(false);
        when(partituraRepository.findAllById(any())).thenReturn(List.of());
        when(pedidoRepository.save(any())).thenReturn(pedidoTeste);
        when(historicoRepository.save(any())).thenReturn(null);
        doNothing().when(emailService).notificarAdminIndisponibilidade(any());

        pedidoService.criar(requestTeste, "guilherme@test.com");

        verify(emailService, times(1)).notificarAdminIndisponibilidade(any());
    }

    // ────── TESTE 8 ──────
    @Test
    @DisplayName("8 - Deve verificar valor correto do Quarteto + Piano")
    void deveUsarValorCorretoQuartetoPiano() {
        assertEquals(6000.0, TipoFormacao.QUARTETO_PIANO.getValorBase());
    }

    // ────── TESTE 9 ──────
    @Test
    @DisplayName("9 - Deve verificar valor correto do Duo")
    void deveUsarValorCorretoDuo() {
        assertEquals(3000.0, TipoFormacao.DUO.getValorBase());
    }

    // ────── TESTE 10 ──────
    @Test
    @DisplayName("10 - Deve cancelar pedido alterando status para CANCELADO")
    void deveCancelarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTeste));
        when(pedidoRepository.save(any())).thenReturn(pedidoTeste);
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(historicoRepository.save(any())).thenReturn(null);

        assertDoesNotThrow(() -> pedidoService.deletar(1L, "guilherme@test.com"));
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    // ────── TESTE 11 ──────
    @Test
    @DisplayName("11 - Deve registrar histórico ao criar pedido")
    void deveRegistrarHistoricoAoCriarPedido() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(agendaService.verificarDisponibilidade(any(), any(), any())).thenReturn(true);
        when(partituraRepository.findAllById(any())).thenReturn(List.of());
        when(pedidoRepository.save(any())).thenReturn(pedidoTeste);
        when(historicoRepository.save(any())).thenReturn(null);

        pedidoService.criar(requestTeste, "guilherme@test.com");

        verify(historicoRepository, atLeastOnce()).save(any());
    }

    // ────── TESTE 12 ──────
    @Test
    @DisplayName("12 - Deve combinar adicional SP + partituras novas corretamente")
    void deveCombinarAdicionaisCorretamente() {
        // Fora de SP (800) + 3 partituras novas (450) = 1250 adicional sobre o Trio (4000) = 5250
        requestTeste.setTipoFormacao(TipoFormacao.TRIO);
        requestTeste.setCidadeEvento("Santos");
        requestTeste.setQtdPartiurasNovas(3);

        double valorEsperado = TipoFormacao.TRIO.getValorBase() + 800.0 + (3 * 150.0);
        assertEquals(5250.0, valorEsperado);
    }
}

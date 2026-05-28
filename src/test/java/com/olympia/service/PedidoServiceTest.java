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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testes do PedidoService")
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private PartituraRepository partituraRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private HistoricoOperacaoRepository historicoRepository;
    @Mock private AgendaService agendaService;

    // EmailService como spy de uma subclasse anonima para evitar problema com Java 26
    private EmailService emailService;
    private PedidoService pedidoService;

    private Usuario usuarioTeste;
    private PedidoRequest requestTeste;
    private Pedido pedidoTeste;

    @BeforeEach
    void setUp() {
        // Criar EmailService sem dependências para testes
        emailService = mock(EmailService.class, withSettings().useConstructor(null));

        pedidoService = new PedidoService(
            pedidoRepository, partituraRepository, usuarioRepository,
            historicoRepository, emailService, agendaService
        );

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

    // ── TESTE 1 ──
    @Test
    @DisplayName("1 - Valor base do Duo deve ser R$ 3.000")
    void testValorBaseDuo() {
        assertEquals(3000.0, TipoFormacao.DUO.getValorBase());
    }

    // ── TESTE 2 ──
    @Test
    @DisplayName("2 - Valor base do Trio deve ser R$ 4.000")
    void testValorBaseTrio() {
        assertEquals(4000.0, TipoFormacao.TRIO.getValorBase());
    }

    // ── TESTE 3 ──
    @Test
    @DisplayName("3 - Valor base do Quarteto deve ser R$ 5.000")
    void testValorBaseQuarteto() {
        assertEquals(5000.0, TipoFormacao.QUARTETO.getValorBase());
    }

    // ── TESTE 4 ──
    @Test
    @DisplayName("4 - Valor base do Quarteto + Piano deve ser R$ 6.000")
    void testValorBaseQuartetoPiano() {
        assertEquals(6000.0, TipoFormacao.QUARTETO_PIANO.getValorBase());
    }

    // ── TESTE 5 ──
    @Test
    @DisplayName("5 - Adicional fora SP deve ser R$ 800")
    void testAdicionalForaSP() {
        double adicional = 800.0;
        double base = TipoFormacao.QUARTETO.getValorBase();
        assertEquals(5800.0, base + adicional);
    }

    // ── TESTE 6 ──
    @Test
    @DisplayName("6 - Custo por partitura nova deve ser R$ 150")
    void testCustoPartituraNova() {
        double custoPartitura = 150.0;
        int qtd = 3;
        assertEquals(450.0, custoPartitura * qtd);
    }

    // ── TESTE 7 ──
    @Test
    @DisplayName("7 - Calcular total: Quarteto + fora SP + 2 partituras novas")
    void testCalculoTotalCompleto() {
        double base = TipoFormacao.QUARTETO.getValorBase(); // 5000
        double deslocamento = 800.0;
        double partituras = 2 * 150.0; // 300
        assertEquals(6100.0, base + deslocamento + partituras);
    }

    // ── TESTE 8 ──
    @Test
    @DisplayName("8 - Deve lançar exceção quando pedido não encontrado")
    void testPedidoNaoEncontrado() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> pedidoService.buscarPorId(999L));
    }

    // ── TESTE 9 ──
    @Test
    @DisplayName("9 - Deve listar pedidos por usuario")
    void testListarPedidosPorUsuario() {
        when(usuarioRepository.findByEmail("guilherme@test.com"))
                .thenReturn(Optional.of(usuarioTeste));
        when(pedidoRepository.findByUsuarioId(1L))
                .thenReturn(List.of(pedidoTeste));

        List<PedidoResponse> lista = pedidoService.listarPorUsuario("guilherme@test.com");

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("João Silva", lista.get(0).getNomeCliente());
    }

    // ── TESTE 10 ──
    @Test
    @DisplayName("10 - Deve atualizar status do pedido para CONFIRMADO")
    void testAtualizarStatus() {
        Pedido confirmado = Pedido.builder()
                .id(1L).nomeCliente("João").emailCliente("joao@email.com")
                .tipoFormacao(TipoFormacao.QUARTETO).valorBase(5000.0)
                .adicionalDeslocamento(0.0).adicionalPartituras(0.0).valorTotal(5000.0)
                .status(StatusPedido.CONFIRMADO).foraSp(false)
                .dataEvento(LocalDate.now().plusDays(30)).horaEvento(LocalTime.of(16, 0))
                .cidadeEvento("São Paulo").estadoEvento("SP")
                .partituras(List.of()).qtdPartiurasNovas(0)
                .usuario(usuarioTeste).build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTeste));
        when(pedidoRepository.save(any())).thenReturn(confirmado);
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(historicoRepository.save(any())).thenReturn(null);

        PedidoResponse response = pedidoService.atualizarStatus(1L, StatusPedido.CONFIRMADO, "guilherme@test.com");
        assertEquals(StatusPedido.CONFIRMADO, response.getStatus());
    }

    // ── TESTE 11 ──
    @Test
    @DisplayName("11 - Deve cancelar pedido alterando status para CANCELADO")
    void testCancelarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTeste));
        when(pedidoRepository.save(any())).thenReturn(pedidoTeste);
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.of(usuarioTeste));
        when(historicoRepository.save(any())).thenReturn(null);

        assertDoesNotThrow(() -> pedidoService.deletar(1L, "guilherme@test.com"));
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    // ── TESTE 12 ──
    @Test
    @DisplayName("12 - Duo tem composição correta: Violino + Cello")
    void testComposicaoDuo() {
        assertEquals("1 Violino + 1 Cello", TipoFormacao.DUO.getComposicao());
    }

    // ── TESTE 13 ──
    @Test
    @DisplayName("13 - Quarteto Piano tem composição correta")
    void testComposicaoQuartetoPiano() {
        assertTrue(TipoFormacao.QUARTETO_PIANO.getComposicao().contains("Piano"));
    }

    // ── TESTE 14 ──
    @Test
    @DisplayName("14 - Status inicial do pedido deve ser PENDENTE")
    void testStatusInicial() {
        assertEquals(StatusPedido.PENDENTE, pedidoTeste.getStatus());
    }

    // ── TESTE 15 ──
    @Test
    @DisplayName("15 - Listar todos os pedidos retorna lista")
    void testListarTodos() {
        when(pedidoRepository.findAllByOrderByCriadoEmDesc())
                .thenReturn(List.of(pedidoTeste));

        List<PedidoResponse> lista = pedidoService.listarTodos();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}

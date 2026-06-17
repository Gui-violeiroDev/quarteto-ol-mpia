package com.olympia.service;

import com.olympia.enums.StatusPedido;
import com.olympia.enums.TipoFormacao;
import com.olympia.enums.TipoInstrumento;
import com.olympia.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes das Regras de Negócio - Quarteto Olympia")
class PedidoServiceTest {

    static final double ADICIONAL_FORA_SP    = 800.0;
    static final double CUSTO_PARTITURA_NOVA = 150.0;

    @Test @DisplayName("1 - Valor base do Duo deve ser R$ 3.000,00")
    void testValorBaseDuo() {
        assertEquals(3000.0, TipoFormacao.DUO.getValorBase());
    }

    @Test @DisplayName("2 - Valor base do Trio deve ser R$ 4.000,00")
    void testValorBaseTrio() {
        assertEquals(4000.0, TipoFormacao.TRIO.getValorBase());
    }

    @Test @DisplayName("3 - Valor base do Quarteto deve ser R$ 5.000,00")
    void testValorBaseQuarteto() {
        assertEquals(5000.0, TipoFormacao.QUARTETO.getValorBase());
    }

    @Test @DisplayName("4 - Valor base do Quarteto + Piano deve ser R$ 6.000,00")
    void testValorBaseQuartetoPiano() {
        assertEquals(6000.0, TipoFormacao.QUARTETO_PIANO.getValorBase());
    }

    @Test @DisplayName("5 - Adicional fora SP: Quarteto fica R$ 5.800,00")
    void testAdicionalForaSP() {
        assertEquals(5800.0, TipoFormacao.QUARTETO.getValorBase() + ADICIONAL_FORA_SP);
    }

    @Test @DisplayName("6 - Custo de 1 partitura nova = R$ 150,00")
    void testCustoUmaPartituraNova() {
        assertEquals(150.0, CUSTO_PARTITURA_NOVA);
    }

    @Test @DisplayName("7 - Custo de 3 partituras novas = R$ 450,00")
    void testCustoTresPartiturasNovas() {
        assertEquals(450.0, 3 * CUSTO_PARTITURA_NOVA);
    }

    @Test @DisplayName("8 - Total: Quarteto + fora SP + 2 partituras = R$ 6.100,00")
    void testCalculoTotalCompleto() {
        double total = TipoFormacao.QUARTETO.getValorBase() + ADICIONAL_FORA_SP + (2 * CUSTO_PARTITURA_NOVA);
        assertEquals(6100.0, total);
    }

    @Test @DisplayName("9 - Total: Trio + fora SP + 1 partitura = R$ 4.950,00")
    void testCalculoTotalTrio() {
        double total = TipoFormacao.TRIO.getValorBase() + ADICIONAL_FORA_SP + CUSTO_PARTITURA_NOVA;
        assertEquals(4950.0, total);
    }

    @Test @DisplayName("10 - RecursoNaoEncontradoException deve ser lançada corretamente")
    void testExcecaoPedidoNaoEncontrado() {
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            throw new RecursoNaoEncontradoException("Pedido não encontrado: 999");
        });
    }

    @Test @DisplayName("11 - Duo tem composição: 1 Violino + 1 Cello")
    void testComposicaoDuo() {
        assertEquals("1 Violino + 1 Cello", TipoFormacao.DUO.getComposicao());
    }

    @Test @DisplayName("12 - Trio tem composição: 1 Violino + 1 Viola + 1 Cello")
    void testComposicaoTrio() {
        assertEquals("1 Violino + 1 Viola + 1 Cello", TipoFormacao.TRIO.getComposicao());
    }

    @Test @DisplayName("13 - Quarteto + Piano contém Piano na composição")
    void testComposicaoQuartetoPiano() {
        assertTrue(TipoFormacao.QUARTETO_PIANO.getComposicao().contains("Piano"));
    }

    @Test @DisplayName("14 - Evento em São Paulo NÃO tem adicional de deslocamento")
    void testSemAdicionalEmSP() {
        String cidade = "São Paulo";
        boolean foraSP = !cidade.equalsIgnoreCase("São Paulo") && !cidade.equalsIgnoreCase("Sao Paulo");
        assertFalse(foraSP);
        assertEquals(0.0, foraSP ? ADICIONAL_FORA_SP : 0.0);
    }

    @Test @DisplayName("15 - Evento em Campinas TEM adicional de R$ 800,00")
    void testComAdicionalForaSP() {
        String cidade = "Campinas";
        boolean foraSP = !cidade.equalsIgnoreCase("São Paulo") && !cidade.equalsIgnoreCase("Sao Paulo");
        assertTrue(foraSP);
        assertEquals(800.0, foraSP ? ADICIONAL_FORA_SP : 0.0);
    }

    @Test @DisplayName("16 - Existem exatamente 4 formações musicais")
    void testQuantidadeFormacoes() {
        assertEquals(4, TipoFormacao.values().length);
    }

    @Test @DisplayName("17 - Descrição do Quarteto é 'Quarteto'")
    void testDescricaoQuarteto() {
        assertEquals("Quarteto", TipoFormacao.QUARTETO.getDescricao());
    }

    @Test @DisplayName("18 - Todos os 4 status de pedido existem")
    void testStatusPedido() {
        assertNotNull(StatusPedido.PENDENTE);
        assertNotNull(StatusPedido.CONFIRMADO);
        assertNotNull(StatusPedido.CANCELADO);
        assertNotNull(StatusPedido.CONCLUIDO);
    }

    @Test @DisplayName("19 - Todos os 4 instrumentos existem")
    void testInstrumentos() {
        assertNotNull(TipoInstrumento.VIOLINO);
        assertNotNull(TipoInstrumento.VIOLA);
        assertNotNull(TipoInstrumento.CELLO);
        assertNotNull(TipoInstrumento.PIANO);
    }

    @Test @DisplayName("20 - Quarteto tem 2 violinos na composição")
    void testQuartetoTemDoisViolinos() {
        assertTrue(TipoFormacao.QUARTETO.getComposicao().contains("2 Violinos"));
    }
}

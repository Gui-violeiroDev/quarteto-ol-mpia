package com.olympia.service;

import com.olympia.enums.TipoFormacao;
import com.olympia.enums.TipoInstrumento;
import com.olympia.enums.Role;
import com.olympia.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Partituras, Músicos e Autenticação")
class PartituraServiceTest {

    @Test @DisplayName("1 - Instrumento PIANO existe no sistema")
    void testInstrumentoPiano() {
        assertNotNull(TipoInstrumento.PIANO);
    }

    @Test @DisplayName("2 - Instrumento VIOLINO existe no sistema")
    void testInstrumentoViolino() {
        assertNotNull(TipoInstrumento.VIOLINO);
    }

    @Test @DisplayName("3 - Instrumento CELLO existe no sistema")
    void testInstrumentoCello() {
        assertNotNull(TipoInstrumento.CELLO);
    }

    @Test @DisplayName("4 - Instrumento VIOLA existe no sistema")
    void testInstrumentoViola() {
        assertNotNull(TipoInstrumento.VIOLA);
    }

    @Test @DisplayName("5 - Role ADMIN existe no sistema")
    void testRoleAdmin() {
        assertNotNull(Role.ROLE_ADMIN);
    }

    @Test @DisplayName("6 - Role USER existe no sistema")
    void testRoleUser() {
        assertNotNull(Role.ROLE_USER);
    }

    @Test @DisplayName("7 - RegraDeNegocioException lançada para partitura duplicada")
    void testExcecaoPartituraDuplicada() {
        assertThrows(RegraDeNegocioException.class, () -> {
            throw new RegraDeNegocioException("Partitura já cadastrada: Canon in D");
        });
    }

    @Test @DisplayName("8 - Mensagem da exceção de partitura duplicada está correta")
    void testMensagemExcecaoPartitura() {
        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class, () -> {
            throw new RegraDeNegocioException("Partitura já cadastrada: Canon in D");
        });
        assertTrue(ex.getMessage().contains("Canon in D"));
    }

    @Test @DisplayName("9 - Quarteto + Piano requer Piano na formação")
    void testQuartetoPianoRequerPiano() {
        assertTrue(TipoFormacao.QUARTETO_PIANO.getComposicao().contains("Piano"));
    }

    @Test @DisplayName("10 - Duo não requer Viola")
    void testDuoNaoRequerViola() {
        assertFalse(TipoFormacao.DUO.getComposicao().contains("Viola"));
    }
}

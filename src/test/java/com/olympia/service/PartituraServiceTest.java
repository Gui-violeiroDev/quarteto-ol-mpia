package com.olympia.service;

import com.olympia.dto.request.PartituraRequest;
import com.olympia.dto.response.PartituraResponse;
import com.olympia.entity.Partitura;
import com.olympia.exception.RegraDeNegocioException;
import com.olympia.repository.PartituraRepository;
import com.olympia.service.impl.PartituraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PartituraService")
class PartituraServiceTest {

    @Mock private PartituraRepository partituraRepository;
    @InjectMocks private PartituraService partituraService;

    @Test
    @DisplayName("Deve listar partituras disponíveis")
    void deveListarPartiturasDisponiveis() {
        Partitura p = Partitura.builder().id(1L).nomeMusica("Canon in D")
                .nomeCompositor("Pachelbel").disponivel(true).build();
        when(partituraRepository.findByDisponivelTrueOrderByNomeMusicaAsc()).thenReturn(List.of(p));

        List<PartituraResponse> lista = partituraService.listarTodas();

        assertEquals(1, lista.size());
        assertEquals("Canon in D", lista.get(0).getNomeMusica());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar partitura duplicada")
    void deveLancarExcecaoPartituraDuplicada() {
        PartituraRequest req = new PartituraRequest();
        req.setNomeMusica("Canon in D");
        req.setNomeCompositor("Pachelbel");

        when(partituraRepository.existsByNomeMusicaIgnoreCase("Canon in D")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> partituraService.criar(req));
    }

    @Test
    @DisplayName("Deve criar partitura com sucesso")
    void deveCriarPartitura() {
        PartituraRequest req = new PartituraRequest();
        req.setNomeMusica("Perfect");
        req.setNomeCompositor("Ed Sheeran");

        Partitura salva = Partitura.builder().id(1L).nomeMusica("Perfect")
                .nomeCompositor("Ed Sheeran").disponivel(true).build();

        when(partituraRepository.existsByNomeMusicaIgnoreCase(any())).thenReturn(false);
        when(partituraRepository.save(any())).thenReturn(salva);

        PartituraResponse response = partituraService.criar(req);

        assertNotNull(response);
        assertEquals("Perfect", response.getNomeMusica());
        assertTrue(response.getDisponivel());
    }
}

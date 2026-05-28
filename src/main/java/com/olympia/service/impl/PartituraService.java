package com.olympia.service.impl;

import com.olympia.dto.request.PartituraRequest;
import com.olympia.dto.response.PartituraResponse;
import com.olympia.entity.Partitura;
import com.olympia.exception.RecursoNaoEncontradoException;
import com.olympia.exception.RegraDeNegocioException;
import com.olympia.repository.PartituraRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PartituraService {
    private final PartituraRepository partituraRepository;
    public PartituraService(PartituraRepository partituraRepository) { this.partituraRepository = partituraRepository; }

    public List<PartituraResponse> listarTodas() {
        return partituraRepository.findByDisponivelTrueOrderByNomeMusicaAsc().stream().map(this::toResponse).toList();
    }
    public PartituraResponse buscarPorId(Long id) { return toResponse(buscarEntidade(id)); }
    public PartituraResponse criar(PartituraRequest request) {
        if (partituraRepository.existsByNomeMusicaIgnoreCase(request.getNomeMusica()))
            throw new RegraDeNegocioException("Partitura já cadastrada: " + request.getNomeMusica());
        Partitura p = Partitura.builder().nomeMusica(request.getNomeMusica())
                .nomeCompositor(request.getNomeCompositor()).disponivel(true).build();
        return toResponse(partituraRepository.save(p));
    }
    public PartituraResponse atualizar(Long id, PartituraRequest request) {
        Partitura p = buscarEntidade(id);
        p.setNomeMusica(request.getNomeMusica()); p.setNomeCompositor(request.getNomeCompositor());
        return toResponse(partituraRepository.save(p));
    }
    public void deletar(Long id) { Partitura p = buscarEntidade(id); p.setDisponivel(false); partituraRepository.save(p); }
    public Partitura buscarEntidade(Long id) {
        return partituraRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Partitura não encontrada: " + id));
    }
    public PartituraResponse toResponse(Partitura p) {
        PartituraResponse r = new PartituraResponse();
        r.setId(p.getId()); r.setNomeMusica(p.getNomeMusica());
        r.setNomeCompositor(p.getNomeCompositor()); r.setDisponivel(p.getDisponivel());
        return r;
    }
}

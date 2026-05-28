package com.olympia.service.impl;

import com.olympia.entity.Musico;
import com.olympia.enums.TipoFormacao;
import com.olympia.enums.TipoInstrumento;
import com.olympia.repository.AgendaMusicoRepository;
import com.olympia.repository.MusicoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendaService {
    private final MusicoRepository musicoRepository;
    private final AgendaMusicoRepository agendaRepository;
    private static final int INTERVALO_HORAS = 4;

    public AgendaService(MusicoRepository musicoRepository, AgendaMusicoRepository agendaRepository) {
        this.musicoRepository = musicoRepository;
        this.agendaRepository = agendaRepository;
    }

    public boolean verificarDisponibilidade(TipoFormacao formacao, LocalDate data, LocalTime hora) {
        LocalTime horaFim = hora.plusHours(INTERVALO_HORAS);
        return switch (formacao) {
            case DUO -> verificarInstrumento(TipoInstrumento.VIOLINO, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.CELLO, data, hora, horaFim, 1);
            case TRIO -> verificarInstrumento(TipoInstrumento.VIOLINO, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.VIOLA, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.CELLO, data, hora, horaFim, 1);
            case QUARTETO -> verificarInstrumento(TipoInstrumento.VIOLINO, data, hora, horaFim, 2)
                    && verificarInstrumento(TipoInstrumento.VIOLA, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.CELLO, data, hora, horaFim, 1);
            case QUARTETO_PIANO -> verificarInstrumento(TipoInstrumento.VIOLINO, data, hora, horaFim, 2)
                    && verificarInstrumento(TipoInstrumento.VIOLA, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.CELLO, data, hora, horaFim, 1)
                    && verificarInstrumento(TipoInstrumento.PIANO, data, hora, horaFim, 1);
        };
    }

    private boolean verificarInstrumento(TipoInstrumento instrumento, LocalDate data,
            LocalTime horaInicio, LocalTime horaFim, int qtdNecessaria) {
        List<Musico> musicos = musicoRepository.findByInstrumentoAndAtivoTrue(instrumento);
        long disponiveis = musicos.stream()
                .filter(m -> agendaRepository.findConflitos(m.getId(), data, horaInicio, horaFim).isEmpty())
                .count();
        return disponiveis >= qtdNecessaria;
    }
}

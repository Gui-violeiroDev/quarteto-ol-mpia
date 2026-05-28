package com.olympia.repository;

import com.olympia.entity.AgendaMusico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendaMusicoRepository extends JpaRepository<AgendaMusico, Long> {

    List<AgendaMusico> findByMusicoIdAndData(Long musicoId, LocalDate data);

    @Query("""
        SELECT a FROM AgendaMusico a
        WHERE a.musico.id = :musicoId
        AND a.data = :data
        AND a.disponivel = false
        AND (
            (:horaInicio >= a.horaInicio AND :horaInicio < a.horaFim)
            OR (:horaFim > a.horaInicio AND :horaFim <= a.horaFim)
        )
    """)
    List<AgendaMusico> findConflitos(
        @Param("musicoId") Long musicoId,
        @Param("data") LocalDate data,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFim") LocalTime horaFim
    );
}

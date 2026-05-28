package com.olympia.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agenda_musicos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"musico_id", "data", "hora_inicio"}))
public class AgendaMusico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "musico_id", nullable = false) private Musico musico;
    @Column(nullable = false) private LocalDate data;
    @Column(name = "hora_inicio", nullable = false) private LocalTime horaInicio;
    @Column(name = "hora_fim", nullable = false) private LocalTime horaFim;
    @Column(nullable = false) private Boolean disponivel = true;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "pedido_id") private Pedido pedido;

    public AgendaMusico() {}
    public Long getId() { return id; }
    public Musico getMusico() { return musico; }
    public void setMusico(Musico v) { this.musico = v; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate v) { this.data = v; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime v) { this.horaInicio = v; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime v) { this.horaFim = v; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean v) { this.disponivel = v; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido v) { this.pedido = v; }
}

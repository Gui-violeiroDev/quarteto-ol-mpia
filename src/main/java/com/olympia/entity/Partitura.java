package com.olympia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partituras")
public class Partitura {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "nome_musica", nullable = false, length = 200) private String nomeMusica;
    @Column(name = "nome_compositor", nullable = false, length = 200) private String nomeCompositor;
    @Column(nullable = false) private Boolean disponivel = true;
    @Column(name = "criado_em", updatable = false) private LocalDateTime criadoEm;

    @PrePersist public void prePersist() { this.criadoEm = LocalDateTime.now(); }

    public Partitura() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeMusica() { return nomeMusica; }
    public void setNomeMusica(String v) { this.nomeMusica = v; }
    public String getNomeCompositor() { return nomeCompositor; }
    public void setNomeCompositor(String v) { this.nomeCompositor = v; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean v) { this.disponivel = v; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Partitura p = new Partitura();
        public Builder id(Long v) { p.id = v; return this; }
        public Builder nomeMusica(String v) { p.nomeMusica = v; return this; }
        public Builder nomeCompositor(String v) { p.nomeCompositor = v; return this; }
        public Builder disponivel(Boolean v) { p.disponivel = v; return this; }
        public Partitura build() { return p; }
    }
}

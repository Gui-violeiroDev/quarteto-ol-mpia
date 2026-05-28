package com.olympia.entity;

import com.olympia.enums.TipoInstrumento;
import jakarta.persistence.*;

@Entity
@Table(name = "musicos")
public class Musico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TipoInstrumento instrumento;
    @Column(length = 150) private String email;
    @Column(length = 20) private String telefone;
    @Column(nullable = false) private Boolean ativo = true;

    public Musico() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoInstrumento getInstrumento() { return instrumento; }
    public void setInstrumento(TipoInstrumento instrumento) { this.instrumento = instrumento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Musico m = new Musico();
        public Builder id(Long v) { m.id = v; return this; }
        public Builder nome(String v) { m.nome = v; return this; }
        public Builder instrumento(TipoInstrumento v) { m.instrumento = v; return this; }
        public Builder email(String v) { m.email = v; return this; }
        public Builder telefone(String v) { m.telefone = v; return this; }
        public Builder ativo(Boolean v) { m.ativo = v; return this; }
        public Musico build() { return m; }
    }
}

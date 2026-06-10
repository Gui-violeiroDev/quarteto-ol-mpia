package com.olympia.entity;

import com.olympia.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100) private String nome;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(nullable = false) private String senha;
    @Column(length = 20) private String telefone;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
    @Column(nullable = false) private Boolean ativo = true;
    @Column(name = "criado_em", updatable = false) private LocalDateTime criadoEm;

    @PrePersist public void prePersist() { this.criadoEm = LocalDateTime.now(); }

    public Usuario() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Usuario u = new Usuario();
        public Builder id(Long v) { u.id = v; return this; }
        public Builder nome(String v) { u.nome = v; return this; }
        public Builder email(String v) { u.email = v; return this; }
        public Builder senha(String v) { u.senha = v; return this; }
        public Builder telefone(String v) { u.telefone = v; return this; }
        public Builder role(Role v) { u.role = v; return this; }
        public Builder ativo(Boolean v) { u.ativo = v; return this; }
        public Usuario build() { return u; }
    }
}

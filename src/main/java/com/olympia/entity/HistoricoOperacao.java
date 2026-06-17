package com.olympia.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_operacoes")
public class HistoricoOperacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String operacao;
    @Column(columnDefinition = "TEXT") private String descricao;
    @Column(name = "tabela_afetada", length = 50) private String tabelaAfetada;
    @Column(name = "registro_id") private Long registroId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
    @Column(name = "realizado_em", updatable = false) private LocalDateTime realizadoEm;

    @PrePersist public void prePersist() { this.realizadoEm = LocalDateTime.now(); }

    public HistoricoOperacao() {}
    public Long getId() { return id; }
    public String getOperacao() { return operacao; }
    public void setOperacao(String v) { this.operacao = v; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String v) { this.descricao = v; }
    public String getTabelaAfetada() { return tabelaAfetada; }
    public void setTabelaAfetada(String v) { this.tabelaAfetada = v; }
    public Long getRegistroId() { return registroId; }
    public void setRegistroId(Long v) { this.registroId = v; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario v) { this.usuario = v; }
    public LocalDateTime getRealizadoEm() { return realizadoEm; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final HistoricoOperacao h = new HistoricoOperacao();
        public Builder operacao(String v) { h.operacao = v; return this; }
        public Builder descricao(String v) { h.descricao = v; return this; }
        public Builder tabelaAfetada(String v) { h.tabelaAfetada = v; return this; }
        public Builder registroId(Long v) { h.registroId = v; return this; }
        public Builder usuario(Usuario v) { h.usuario = v; return this; }
        public HistoricoOperacao build() { return h; }
    }
}
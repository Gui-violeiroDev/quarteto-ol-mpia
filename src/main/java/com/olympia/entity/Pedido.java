package com.olympia.entity;

import com.olympia.enums.StatusPedido;
import com.olympia.enums.TipoFormacao;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "nome_cliente", nullable = false, length = 100) private String nomeCliente;
    @Column(name = "email_cliente", nullable = false, length = 150) private String emailCliente;
    @Column(name = "telefone_cliente", length = 20) private String telefoneCliente;
    @Column(name = "tipo_evento", length = 100) private String tipoEvento;
    @Column(name = "data_evento", nullable = false) private LocalDate dataEvento;
    @Column(name = "hora_evento", nullable = false) private LocalTime horaEvento;
    @Column(name = "endereco_evento", length = 300) private String enderecoEvento;
    @Column(name = "cidade_evento", length = 100) private String cidadeEvento;
    @Column(name = "estado_evento", length = 2) private String estadoEvento;
    @Column(name = "cep_evento", length = 10) private String cepEvento;
    @Column(name = "fora_sp", nullable = false) private Boolean foraSp = false;
    @Enumerated(EnumType.STRING) @Column(name = "tipo_formacao", nullable = false) private TipoFormacao tipoFormacao;
    @ManyToMany
    @JoinTable(name = "pedido_partituras",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "partitura_id"))
    private List<Partitura> partituras = new ArrayList<>();
    @Column(name = "qtd_partituras_novas") private Integer qtdPartiurasNovas = 0;
    @Column(name = "valor_base", nullable = false) private Double valorBase;
    @Column(name = "adicional_deslocamento") private Double adicionalDeslocamento = 0.0;
    @Column(name = "adicional_partituras") private Double adicionalPartituras = 0.0;
    @Column(name = "valor_total", nullable = false) private Double valorTotal;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private StatusPedido status;
    @Column(name = "observacoes", columnDefinition = "TEXT") private String observacoes;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "usuario_id") private Usuario usuario;
    @Column(name = "criado_em", updatable = false) private LocalDateTime criadoEm;
    @Column(name = "atualizado_em") private LocalDateTime atualizadoEm;

    @PrePersist public void prePersist() { this.criadoEm = LocalDateTime.now(); this.atualizadoEm = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { this.atualizadoEm = LocalDateTime.now(); }

    public Pedido() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String v) { this.nomeCliente = v; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String v) { this.emailCliente = v; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String v) { this.telefoneCliente = v; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String v) { this.tipoEvento = v; }
    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate v) { this.dataEvento = v; }
    public LocalTime getHoraEvento() { return horaEvento; }
    public void setHoraEvento(LocalTime v) { this.horaEvento = v; }
    public String getEnderecoEvento() { return enderecoEvento; }
    public void setEnderecoEvento(String v) { this.enderecoEvento = v; }
    public String getCidadeEvento() { return cidadeEvento; }
    public void setCidadeEvento(String v) { this.cidadeEvento = v; }
    public String getEstadoEvento() { return estadoEvento; }
    public void setEstadoEvento(String v) { this.estadoEvento = v; }
    public String getCepEvento() { return cepEvento; }
    public void setCepEvento(String v) { this.cepEvento = v; }
    public Boolean getForaSp() { return foraSp; }
    public void setForaSp(Boolean v) { this.foraSp = v; }
    public TipoFormacao getTipoFormacao() { return tipoFormacao; }
    public void setTipoFormacao(TipoFormacao v) { this.tipoFormacao = v; }
    public List<Partitura> getPartituras() { return partituras; }
    public void setPartituras(List<Partitura> v) { this.partituras = v; }
    public Integer getQtdPartiurasNovas() { return qtdPartiurasNovas; }
    public void setQtdPartiurasNovas(Integer v) { this.qtdPartiurasNovas = v; }
    public Double getValorBase() { return valorBase; }
    public void setValorBase(Double v) { this.valorBase = v; }
    public Double getAdicionalDeslocamento() { return adicionalDeslocamento; }
    public void setAdicionalDeslocamento(Double v) { this.adicionalDeslocamento = v; }
    public Double getAdicionalPartituras() { return adicionalPartituras; }
    public void setAdicionalPartituras(Double v) { this.adicionalPartituras = v; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double v) { this.valorTotal = v; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido v) { this.status = v; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String v) { this.observacoes = v; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario v) { this.usuario = v; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Pedido p = new Pedido();
        public Builder id(Long v) { p.id = v; return this; }
        public Builder nomeCliente(String v) { p.nomeCliente = v; return this; }
        public Builder emailCliente(String v) { p.emailCliente = v; return this; }
        public Builder telefoneCliente(String v) { p.telefoneCliente = v; return this; }
        public Builder tipoEvento(String v) { p.tipoEvento = v; return this; }
        public Builder dataEvento(LocalDate v) { p.dataEvento = v; return this; }
        public Builder horaEvento(LocalTime v) { p.horaEvento = v; return this; }
        public Builder enderecoEvento(String v) { p.enderecoEvento = v; return this; }
        public Builder cidadeEvento(String v) { p.cidadeEvento = v; return this; }
        public Builder estadoEvento(String v) { p.estadoEvento = v; return this; }
        public Builder cepEvento(String v) { p.cepEvento = v; return this; }
        public Builder foraSp(Boolean v) { p.foraSp = v; return this; }
        public Builder tipoFormacao(TipoFormacao v) { p.tipoFormacao = v; return this; }
        public Builder partituras(List<Partitura> v) { p.partituras = v; return this; }
        public Builder qtdPartiurasNovas(Integer v) { p.qtdPartiurasNovas = v; return this; }
        public Builder valorBase(Double v) { p.valorBase = v; return this; }
        public Builder adicionalDeslocamento(Double v) { p.adicionalDeslocamento = v; return this; }
        public Builder adicionalPartituras(Double v) { p.adicionalPartituras = v; return this; }
        public Builder valorTotal(Double v) { p.valorTotal = v; return this; }
        public Builder status(StatusPedido v) { p.status = v; return this; }
        public Builder observacoes(String v) { p.observacoes = v; return this; }
        public Builder usuario(Usuario v) { p.usuario = v; return this; }
        public Pedido build() { return p; }
    }
}

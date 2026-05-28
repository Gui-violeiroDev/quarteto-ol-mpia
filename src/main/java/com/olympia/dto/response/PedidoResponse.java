package com.olympia.dto.response;
import com.olympia.enums.StatusPedido;
import com.olympia.enums.TipoFormacao;
import java.time.LocalDate; import java.time.LocalDateTime; import java.time.LocalTime; import java.util.List;
public class PedidoResponse {
    private Long id; private String nomeCliente; private String emailCliente; private String telefoneCliente;
    private String tipoEvento; private LocalDate dataEvento; private LocalTime horaEvento;
    private String enderecoEvento; private String cidadeEvento; private String estadoEvento; private String cepEvento;
    private Boolean foraSp; private TipoFormacao tipoFormacao; private String tipoFormacaoDescricao;
    private List<PartituraResponse> partituras; private Integer qtdPartiurasNovas;
    private Double valorBase; private Double adicionalDeslocamento; private Double adicionalPartituras; private Double valorTotal;
    private StatusPedido status; private String observacoes; private LocalDateTime criadoEm; private LocalDateTime atualizadoEm;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getNomeCliente() { return nomeCliente; } public void setNomeCliente(String v) { this.nomeCliente = v; }
    public String getEmailCliente() { return emailCliente; } public void setEmailCliente(String v) { this.emailCliente = v; }
    public String getTelefoneCliente() { return telefoneCliente; } public void setTelefoneCliente(String v) { this.telefoneCliente = v; }
    public String getTipoEvento() { return tipoEvento; } public void setTipoEvento(String v) { this.tipoEvento = v; }
    public LocalDate getDataEvento() { return dataEvento; } public void setDataEvento(LocalDate v) { this.dataEvento = v; }
    public LocalTime getHoraEvento() { return horaEvento; } public void setHoraEvento(LocalTime v) { this.horaEvento = v; }
    public String getEnderecoEvento() { return enderecoEvento; } public void setEnderecoEvento(String v) { this.enderecoEvento = v; }
    public String getCidadeEvento() { return cidadeEvento; } public void setCidadeEvento(String v) { this.cidadeEvento = v; }
    public String getEstadoEvento() { return estadoEvento; } public void setEstadoEvento(String v) { this.estadoEvento = v; }
    public String getCepEvento() { return cepEvento; } public void setCepEvento(String v) { this.cepEvento = v; }
    public Boolean getForaSp() { return foraSp; } public void setForaSp(Boolean v) { this.foraSp = v; }
    public TipoFormacao getTipoFormacao() { return tipoFormacao; } public void setTipoFormacao(TipoFormacao v) { this.tipoFormacao = v; }
    public String getTipoFormacaoDescricao() { return tipoFormacaoDescricao; } public void setTipoFormacaoDescricao(String v) { this.tipoFormacaoDescricao = v; }
    public List<PartituraResponse> getPartituras() { return partituras; } public void setPartituras(List<PartituraResponse> v) { this.partituras = v; }
    public Integer getQtdPartiurasNovas() { return qtdPartiurasNovas; } public void setQtdPartiurasNovas(Integer v) { this.qtdPartiurasNovas = v; }
    public Double getValorBase() { return valorBase; } public void setValorBase(Double v) { this.valorBase = v; }
    public Double getAdicionalDeslocamento() { return adicionalDeslocamento; } public void setAdicionalDeslocamento(Double v) { this.adicionalDeslocamento = v; }
    public Double getAdicionalPartituras() { return adicionalPartituras; } public void setAdicionalPartituras(Double v) { this.adicionalPartituras = v; }
    public Double getValorTotal() { return valorTotal; } public void setValorTotal(Double v) { this.valorTotal = v; }
    public StatusPedido getStatus() { return status; } public void setStatus(StatusPedido v) { this.status = v; }
    public String getObservacoes() { return observacoes; } public void setObservacoes(String v) { this.observacoes = v; }
    public LocalDateTime getCriadoEm() { return criadoEm; } public void setCriadoEm(LocalDateTime v) { this.criadoEm = v; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; } public void setAtualizadoEm(LocalDateTime v) { this.atualizadoEm = v; }
}

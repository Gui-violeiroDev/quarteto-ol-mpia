package com.olympia.dto.request;
import com.olympia.enums.TipoFormacao;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PedidoRequest {
    @NotBlank(message = "Nome do cliente é obrigatório") private String nomeCliente;
    @NotBlank(message = "Email é obrigatório") @Email private String emailCliente;
    @NotBlank(message = "Telefone é obrigatório") private String telefoneCliente;
    @NotBlank(message = "Tipo de evento é obrigatório") private String tipoEvento;
    @NotNull(message = "Data do evento é obrigatória") @Future private LocalDate dataEvento;
    @NotNull(message = "Hora do evento é obrigatória") private LocalTime horaEvento;
    @NotBlank(message = "Endereço é obrigatório") private String enderecoEvento;
    @NotBlank(message = "Cidade é obrigatória") private String cidadeEvento;
    @NotBlank(message = "Estado é obrigatório") @Size(min = 2, max = 2) private String estadoEvento;
    @NotBlank(message = "CEP é obrigatório") private String cepEvento;
    @NotNull(message = "Tipo de formação é obrigatório") private TipoFormacao tipoFormacao;
    private List<Long> partituraIds;
    private Integer qtdPartiurasNovas = 0;
    private String observacoes;

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
    public TipoFormacao getTipoFormacao() { return tipoFormacao; } public void setTipoFormacao(TipoFormacao v) { this.tipoFormacao = v; }
    public List<Long> getPartituraIds() { return partituraIds; } public void setPartituraIds(List<Long> v) { this.partituraIds = v; }
    public Integer getQtdPartiurasNovas() { return qtdPartiurasNovas; } public void setQtdPartiurasNovas(Integer v) { this.qtdPartiurasNovas = v; }
    public String getObservacoes() { return observacoes; } public void setObservacoes(String v) { this.observacoes = v; }
}

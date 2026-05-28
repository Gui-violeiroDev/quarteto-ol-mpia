package com.olympia.dto.request;
import jakarta.validation.constraints.NotBlank;

public class PartituraRequest {
    @NotBlank(message = "Nome da música é obrigatório")
    private String nomeMusica;
    @NotBlank(message = "Nome do compositor é obrigatório")
    private String nomeCompositor;

    public String getNomeMusica() { return nomeMusica; }
    public void setNomeMusica(String v) { this.nomeMusica = v; }
    public String getNomeCompositor() { return nomeCompositor; }
    public void setNomeCompositor(String v) { this.nomeCompositor = v; }
}

package com.olympia.dto.response;
public class PartituraResponse {
    private Long id; private String nomeMusica; private String nomeCompositor; private Boolean disponivel;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getNomeMusica() { return nomeMusica; } public void setNomeMusica(String v) { this.nomeMusica = v; }
    public String getNomeCompositor() { return nomeCompositor; } public void setNomeCompositor(String v) { this.nomeCompositor = v; }
    public Boolean getDisponivel() { return disponivel; } public void setDisponivel(Boolean v) { this.disponivel = v; }
}

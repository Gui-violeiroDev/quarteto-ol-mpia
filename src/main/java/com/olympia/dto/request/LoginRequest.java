package com.olympia.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getSenha() { return senha; }
    public void setSenha(String v) { this.senha = v; }
}

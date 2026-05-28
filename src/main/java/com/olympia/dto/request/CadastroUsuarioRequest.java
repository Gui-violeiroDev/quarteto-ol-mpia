package com.olympia.dto.request;
import com.olympia.enums.Role;
import jakarta.validation.constraints.*;

public class CadastroUsuarioRequest {
    @NotBlank(message = "Nome é obrigatório") @Size(min = 3, max = 100)
    private String nome;
    @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha é obrigatória") @Size(min = 6)
    private String senha;
    @Size(max = 20)
    private String telefone;
    private Role role = Role.ROLE_USER;

    public String getNome() { return nome; } public void setNome(String v) { this.nome = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
    public String getSenha() { return senha; } public void setSenha(String v) { this.senha = v; }
    public String getTelefone() { return telefone; } public void setTelefone(String v) { this.telefone = v; }
    public Role getRole() { return role; } public void setRole(Role v) { this.role = v; }
}

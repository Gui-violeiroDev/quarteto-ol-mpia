package com.olympia.dto.response;
import com.olympia.enums.Role;
public class AuthResponse {
    private String token; private String tipo = "Bearer";
    private String nome; private String email; private Role role;
    public String getToken() { return token; } public void setToken(String v) { this.token = v; }
    public String getTipo() { return tipo; } public void setTipo(String v) { this.tipo = v; }
    public String getNome() { return nome; } public void setNome(String v) { this.nome = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
    public Role getRole() { return role; } public void setRole(Role v) { this.role = v; }
}

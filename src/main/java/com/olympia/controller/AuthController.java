package com.olympia.controller;
import com.olympia.dto.request.CadastroUsuarioRequest;
import com.olympia.dto.request.LoginRequest;
import com.olympia.dto.response.ApiResponse;
import com.olympia.dto.response.AuthResponse;
import com.olympia.service.impl.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Login realizado com sucesso", authService.login(request)));
    }
    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<AuthResponse>> cadastrar(@Valid @RequestBody CadastroUsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Usuário cadastrado com sucesso", authService.cadastrar(request)));
    }
}

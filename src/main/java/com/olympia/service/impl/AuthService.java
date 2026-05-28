package com.olympia.service.impl;

import com.olympia.dto.request.CadastroUsuarioRequest;
import com.olympia.dto.request.LoginRequest;
import com.olympia.dto.response.AuthResponse;
import com.olympia.entity.Usuario;
import com.olympia.enums.Role;
import com.olympia.exception.RegraDeNegocioException;
import com.olympia.repository.UsuarioRepository;
import com.olympia.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, AuthenticationManager authManager, UserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.gerarToken(userDetails);
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        AuthResponse r = new AuthResponse();
        r.setToken(token); r.setTipo("Bearer");
        r.setNome(usuario.getNome()); r.setEmail(usuario.getEmail()); r.setRole(usuario.getRole());
        return r;
    }

    public AuthResponse cadastrar(CadastroUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new RegraDeNegocioException("Email já cadastrado: " + request.getEmail());
        Usuario usuario = Usuario.builder()
                .nome(request.getNome()).email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .telefone(request.getTelefone())
                .role(request.getRole() != null ? request.getRole() : Role.ROLE_USER)
                .ativo(true).build();
        usuarioRepository.save(usuario);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.gerarToken(userDetails);
        AuthResponse r = new AuthResponse();
        r.setToken(token); r.setTipo("Bearer");
        r.setNome(usuario.getNome()); r.setEmail(usuario.getEmail()); r.setRole(usuario.getRole());
        return r;
    }
}

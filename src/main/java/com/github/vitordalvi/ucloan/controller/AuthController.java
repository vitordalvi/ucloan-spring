package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.UserAuthenticationRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.dto.response.AuthenticationResponseDto;
import com.github.vitordalvi.ucloan.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint de registro para usuários
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(
            @Valid @RequestBody UserRegisterRequestDto dto) {

        return ResponseEntity.ok(authService.register(dto));
    }

    // Endpoint de autenticação de usuário já criado
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Valid @RequestBody UserAuthenticationRequestDto dto) {
        return ResponseEntity.ok(authService.authenticate(dto));
    }

    /*
        Criar o Endpoint de forget-password
    */

    // Endpoint para dar refresh no token do usuário
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refresh(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        AuthenticationResponseDto response = authService.refreshToken(authHeader);

        return ResponseEntity.ok(response);
    }
}

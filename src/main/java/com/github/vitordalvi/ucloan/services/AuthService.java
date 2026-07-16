package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.config.JwtService;
import com.github.vitordalvi.ucloan.dto.request.UserAuthenticationRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.dto.response.AuthenticationResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.Token;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.entities.enums.TokenType;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.mapper.TokenMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import com.github.vitordalvi.ucloan.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class AuthService {

    private final ApplicationUserRepository applicationUserRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserMapper userMapper;
    private final TokenMapper tokenMapper;

    public AuthService(ApplicationUserRepository applicationUserRepository,
                       TokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       ApplicationUserMapper userMapper,
                       TokenMapper tokenMapper) {

        this.applicationUserRepository = applicationUserRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.tokenMapper = tokenMapper;
    }

    public AuthenticationResponseDto register(UserRegisterRequestDto request) {
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        var savedUser = applicationUserRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, jwtToken);

        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    public AuthenticationResponseDto authenticate(UserAuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = applicationUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Any records found for this email!"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    private void saveUserToken(ApplicationUser user, String jwtToken) {
        var token = tokenMapper.toEntity(user, jwtToken);

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(ApplicationUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponseDto refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header inválido");
        }

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new ResourceNotFoundException("Token de atualização inválido");
        }

        var user = applicationUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token expirado ou inválido");
        }

        var accessToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}

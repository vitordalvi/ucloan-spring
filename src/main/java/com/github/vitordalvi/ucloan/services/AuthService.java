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

    // Função de registro de um usuário
    public AuthenticationResponseDto register(UserRegisterRequestDto request) {
        var user = userMapper.toEntity(request); // Cria a entidade do usuário com os dados do dto

        user.setPassword(passwordEncoder.encode(request.password())); // Seta a senha do usuário já criptografando
        user.setRole(Role.USER); // Seta o cargo padrão do usuário

        var savedUser = applicationUserRepository.save(user); // Salva o usuário
        var jwtToken = jwtService.generateToken(savedUser); // Gera o token JWT do o usuário
        var refreshToken = jwtService.generateRefreshToken(savedUser); // Gera o refresh token do usuário

        saveUserToken(savedUser, jwtToken); // Salva o token e associa ao usuário

        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    // Função de login do usuário
    public AuthenticationResponseDto authenticate(UserAuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Procura o usuário no banco, pelo email
        var user = applicationUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this email!"));

        var jwtToken = jwtService.generateToken(user); // Gera o token para o usuário
        var refreshToken = jwtService.generateRefreshToken(user); // Gera o refresh token
        revokeAllUserTokens(user); // Revoga todos os tokens atuais do usuário
        saveUserToken(user, jwtToken); // Salva o novo token ao usuario

        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    // Salva o token e associa ao usuário
    private void saveUserToken(ApplicationUser user, String jwtToken) {
        var token = tokenMapper.toEntity(user, jwtToken); // Transforma o token em entidade, associa o usuário ao token

        tokenRepository.save(token); // Salva o token no banco
    }

    // Revoga todos os tokens do usuário
    private void revokeAllUserTokens(ApplicationUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId()); // Armazena todos os tokens do usuário

        // Se for nulo, termina a função aqui
        if (validUserTokens.isEmpty()) {
            return;
        }

        // Percorre a lista de tokens do usuário e revoga todos eles
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        // Salva a lista de tokens no banco
        tokenRepository.saveAll(validUserTokens);
    }

    // Retorna os tokens atualizados do usuário
    public AuthenticationResponseDto refreshToken(String authHeader) {
        // Se o header for nulo ou não começar com o padrão do Authorization, retorna exception
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header inválido");
        }

        String refreshToken = authHeader.substring(7); // Armazena o token
        String userEmail = jwtService.extractUsername(refreshToken); // Armazena o username (email) do usuário pelas claims

        // Se o email for nulo, retorna exception
        if (userEmail == null) {
            throw new ResourceNotFoundException("Token de atualização inválido");
        }

        // Armazena o usuário, pegando pelo username do header
        var user = applicationUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado"));

        // Se o token não for válido, retorna exception
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token expirado ou inválido");
        }

        // Gera o novo token de acesso do usuário
        var accessToken = jwtService.generateToken(user);

        // Revoga todos os tokens antigos
        revokeAllUserTokens(user);
        // Salva novos tokens
        saveUserToken(user, accessToken);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}

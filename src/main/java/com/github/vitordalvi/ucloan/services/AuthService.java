package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.config.JwtService;
import com.github.vitordalvi.ucloan.dto.request.UserAuthenticationRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.dto.response.AuthenticationResponseDto;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserMapper userMapper;

    public AuthService(ApplicationUserRepository applicationUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       ApplicationUserMapper userMapper) {

        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    // Função de registro de um novo usuário com emissão de tokens stateless
    @Transactional
    public AuthenticationResponseDto register(UserRegisterRequestDto request) {
        log.info("Trying to register user with email: {}", request.email());

        if (applicationUserRepository.existsByEmailAndEnabledTrue(request.email())) {
            log.error("This e-mail is currently in use: {}", request.email());
            throw new BusinessException("This e-mail is currently in use!");
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        var savedUser = applicationUserRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        log.info("User with e-mail: {} was registered", request.email());
        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    // Função de login com emissão de tokens stateless (sem persistência em banco)
    public AuthenticationResponseDto authenticate(UserAuthenticationRequestDto dto) {
        log.info("User is trying to authenticate with e-mail: {}", dto.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        var user = applicationUserRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this email!"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        log.info("User authenticated with e-mail: {}", user.getEmail());
        return new AuthenticationResponseDto(jwtToken, refreshToken);
    }

    // Renova o par de tokens (Access Token e Refresh Token com rotation) de forma stateless
    public AuthenticationResponseDto refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid headers");
        }

        String refreshToken = authHeader.substring(7);
        String userEmail;

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid refresh token!", e);
        }

        if (userEmail == null) {
            throw new ResourceNotFoundException("No records found for this email!");
        }

        var user = applicationUserRepository.findByEmailAndEnabledTrue(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No users found for this username"));

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid refresh token!");
        }

        log.info("User with e-mail: {} is refreshing their token", user.getEmail());
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponseDto(newAccessToken, newRefreshToken);
    }
}

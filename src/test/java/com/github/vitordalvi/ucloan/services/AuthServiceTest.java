package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.config.JwtService;
import com.github.vitordalvi.ucloan.dto.request.UserAuthenticationRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserRegisterRequestDto;
import com.github.vitordalvi.ucloan.dto.response.AuthenticationResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationUserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        user.setEmail("user@ucloan.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.USER);
    }

    @Nested
    @DisplayName("register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should throw BusinessException when email is already in use")
        void shouldThrowExceptionWhenEmailAlreadyInUse() {
            UserRegisterRequestDto request = new UserRegisterRequestDto("John", "Doe", "user@ucloan.com", "password123");

            when(applicationUserRepository.existsByEmailAndEnabledTrue(request.email())).thenReturn(true);

            assertThrows(BusinessException.class, () -> authService.register(request));
            verify(applicationUserRepository, never()).save(any());
            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should register user and return stateless access and refresh tokens")
        void shouldRegisterUserSuccessfully() {
            UserRegisterRequestDto request = new UserRegisterRequestDto("John", "Doe", "user@ucloan.com", "password123");

            when(applicationUserRepository.existsByEmailAndEnabledTrue(request.email())).thenReturn(false);
            when(userMapper.toEntity(request)).thenReturn(user);
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(applicationUserRepository.save(user)).thenReturn(user);
            when(jwtService.generateToken(user)).thenReturn("mockAccessToken");
            when(jwtService.generateRefreshToken(user)).thenReturn("mockRefreshToken");

            AuthenticationResponseDto response = authService.register(request);

            assertNotNull(response);
            assertEquals("mockAccessToken", response.accessToken());
            assertEquals("mockRefreshToken", response.refreshToken());
            assertEquals("encodedPassword", user.getPassword());
            assertEquals(Role.USER, user.getRole());

            verify(applicationUserRepository, times(1)).save(user);
        }
    }

    @Nested
    @DisplayName("authenticate Tests")
    class AuthenticateTests {

        @Test
        @DisplayName("Should authenticate and return stateless tokens")
        void shouldAuthenticateSuccessfully() {
            UserAuthenticationRequestDto request = new UserAuthenticationRequestDto("user@ucloan.com", "password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
            when(applicationUserRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
            when(jwtService.generateToken(user)).thenReturn("newAccessToken");
            when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

            AuthenticationResponseDto response = authService.authenticate(request);

            assertNotNull(response);
            assertEquals("newAccessToken", response.accessToken());
            assertEquals("newRefreshToken", response.refreshToken());
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when email does not exist")
        void shouldThrowExceptionWhenEmailNotFound() {
            UserAuthenticationRequestDto request = new UserAuthenticationRequestDto("notfound@ucloan.com", "password123");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
            when(applicationUserRepository.findByEmail(request.email())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authService.authenticate(request));
            verify(jwtService, never()).generateToken(any());
        }
    }

    @Nested
    @DisplayName("refreshToken Tests")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when header is missing or does not start with Bearer")
        void shouldThrowExceptionWhenHeaderIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(null));
            assertThrows(IllegalArgumentException.class, () -> authService.refreshToken("Basic 123456"));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user is not found for refresh token email")
        void shouldThrowExceptionWhenUserNotFound() {
            String authHeader = "Bearer validRefreshToken";
            when(jwtService.extractUsername("validRefreshToken")).thenReturn("user@ucloan.com");
            when(applicationUserRepository.findByEmailAndEnabledTrue("user@ucloan.com")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> authService.refreshToken(authHeader));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when refresh token is invalid or wrong type")
        void shouldThrowExceptionWhenRefreshTokenInvalid() {
            String authHeader = "Bearer invalidRefreshToken";
            when(jwtService.extractUsername("invalidRefreshToken")).thenReturn("user@ucloan.com");
            when(applicationUserRepository.findByEmailAndEnabledTrue("user@ucloan.com")).thenReturn(Optional.of(user));
            when(jwtService.isRefreshTokenValid("invalidRefreshToken", user)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> authService.refreshToken(authHeader));
        }

        @Test
        @DisplayName("Should return rotated access and refresh tokens when valid")
        void shouldRefreshTokenSuccessfully() {
            String authHeader = "Bearer validRefreshToken";
            when(jwtService.extractUsername("validRefreshToken")).thenReturn("user@ucloan.com");
            when(applicationUserRepository.findByEmailAndEnabledTrue("user@ucloan.com")).thenReturn(Optional.of(user));
            when(jwtService.isRefreshTokenValid("validRefreshToken", user)).thenReturn(true);
            when(jwtService.generateToken(user)).thenReturn("brandNewAccessToken");
            when(jwtService.generateRefreshToken(user)).thenReturn("brandNewRefreshToken");

            AuthenticationResponseDto response = authService.refreshToken(authHeader);

            assertNotNull(response);
            assertEquals("brandNewAccessToken", response.accessToken());
            assertEquals("brandNewRefreshToken", response.refreshToken());
        }
    }
}

package com.github.vitordalvi.ucloan.config;

import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.entities.enums.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long ACCESS_EXPIRATION = 900_000L; // 15 minutos
    private static final long REFRESH_EXPIRATION = 604_800_000L; // 7 dias

    private JwtService jwtService;
    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY, ACCESS_EXPIRATION, REFRESH_EXPIRATION);

        user = new ApplicationUser();
        user.setEmail("test@ucloan.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setRole(Role.USER);
    }

    @Nested
    @DisplayName("generateToken Tests")
    class GenerateTokenTests {

        @Test
        @DisplayName("Should generate valid stateless access token with ACCESS type and correct subject")
        void shouldGenerateValidAccessToken() {
            String token = jwtService.generateToken(user);

            assertNotNull(token);
            assertEquals("test@ucloan.com", jwtService.extractUsername(token));
            assertEquals(TokenType.ACCESS.name(), jwtService.extractTokenType(token));
            assertTrue(jwtService.isAccessToken(token));
            assertFalse(jwtService.isRefreshToken(token));
            assertTrue(jwtService.isTokenValid(token, user));

            Date expiration = jwtService.extractExpiration(token);
            assertTrue(expiration.after(new Date()));
        }

        @Test
        @DisplayName("Should generate valid stateless refresh token with REFRESH type and correct subject")
        void shouldGenerateValidRefreshToken() {
            String refreshToken = jwtService.generateRefreshToken(user);

            assertNotNull(refreshToken);
            assertEquals("test@ucloan.com", jwtService.extractUsername(refreshToken));
            assertEquals(TokenType.REFRESH.name(), jwtService.extractTokenType(refreshToken));
            assertTrue(jwtService.isRefreshToken(refreshToken));
            assertFalse(jwtService.isAccessToken(refreshToken));
            assertTrue(jwtService.isRefreshTokenValid(refreshToken, user));

            Date expiration = jwtService.extractExpiration(refreshToken);
            assertTrue(expiration.after(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION)));
        }
    }

    @Nested
    @DisplayName("isTokenValid Tests")
    class IsTokenValidTests {

        @Test
        @DisplayName("Should return false when refresh token is used as access token")
        void shouldReturnFalseWhenRefreshTokenUsedAsAccessToken() {
            String refreshToken = jwtService.generateRefreshToken(user);

            assertFalse(jwtService.isTokenValid(refreshToken, user));
        }

        @Test
        @DisplayName("Should return false when user does not match token subject")
        void shouldReturnFalseWhenUserDoesNotMatch() {
            String token = jwtService.generateToken(user);

            ApplicationUser otherUser = new ApplicationUser();
            otherUser.setEmail("other@ucloan.com");

            assertFalse(jwtService.isTokenValid(token, otherUser));
        }

        @Test
        @DisplayName("Should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            JwtService expiredJwtService = new JwtService(SECRET_KEY, -1000L, -1000L);
            String expiredToken = expiredJwtService.generateToken(user);

            assertFalse(jwtService.isTokenValid(expiredToken, user));
            assertTrue(jwtService.isTokenExpired(expiredToken));
        }

        @Test
        @DisplayName("Should return false for malformed or tampered token")
        void shouldReturnFalseForMalformedToken() {
            assertFalse(jwtService.isTokenValid("invalid.token.structure", user));
        }
    }

    @Nested
    @DisplayName("isRefreshTokenValid Tests")
    class IsRefreshTokenValidTests {

        @Test
        @DisplayName("Should return false when access token is used as refresh token")
        void shouldReturnFalseWhenAccessTokenUsedAsRefreshToken() {
            String accessToken = jwtService.generateToken(user);

            assertFalse(jwtService.isRefreshTokenValid(accessToken, user));
        }

        @Test
        @DisplayName("Should return false when user does not match refresh token subject")
        void shouldReturnFalseWhenUserDoesNotMatchRefreshToken() {
            String refreshToken = jwtService.generateRefreshToken(user);

            ApplicationUser otherUser = new ApplicationUser();
            otherUser.setEmail("other@ucloan.com");

            assertFalse(jwtService.isRefreshTokenValid(refreshToken, otherUser));
        }

        @Test
        @DisplayName("Should return false for expired refresh token")
        void shouldReturnFalseForExpiredRefreshToken() {
            JwtService expiredJwtService = new JwtService(SECRET_KEY, -1000L, -1000L);
            String expiredRefreshToken = expiredJwtService.generateRefreshToken(user);

            assertFalse(jwtService.isRefreshTokenValid(expiredRefreshToken, user));
        }
    }
}

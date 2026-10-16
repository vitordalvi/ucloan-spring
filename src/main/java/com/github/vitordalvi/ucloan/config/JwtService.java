package com.github.vitordalvi.ucloan.config;

import com.github.vitordalvi.ucloan.entities.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String CLAIM_TOKEN_TYPE = "token_type";

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public JwtService() {}

    public JwtService(String secretKey, long jwtExpiration, long refreshExpiration) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    // Função para extrair o username (e-mail) do usuário pelo token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai uma claim específica
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrai o tipo de token (ACCESS ou REFRESH)
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_TOKEN_TYPE, String.class));
    }

    // Verifica se o token informado é um access token
    public boolean isAccessToken(String token) {
        String type = extractTokenType(token);
        return TokenType.ACCESS.name().equals(type) || TokenType.BEARER.name().equals(type);
    }

    // Verifica se o token informado é um refresh token
    public boolean isRefreshToken(String token) {
        return TokenType.REFRESH.name().equals(extractTokenType(token));
    }

    // Gera o Access Token stateless padrão do usuário
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Gera o Access Token stateless com claims extras
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put(CLAIM_TOKEN_TYPE, TokenType.ACCESS.name());
        return buildToken(claims, userDetails, jwtExpiration);
    }

    // Gera o Refresh Token stateless do usuário
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TOKEN_TYPE, TokenType.REFRESH.name());
        return buildToken(claims, userDetails, refreshExpiration);
    }

    // Construção do token JWT assinado
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    // Validação stateless do Access Token: valida assinatura, expiração, username e tipo
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && isAccessToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Validação stateless do Refresh Token: valida assinatura, expiração, username e tipo
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && isRefreshToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Retorna se o token está ou não expirado
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // Retorna a expiration do token pela claim exp
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai e valida a assinatura de todas as claims enviadas no token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Decodifica a secret key do sistema para geração e validação de assinaturas
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

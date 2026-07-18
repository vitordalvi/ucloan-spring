package com.github.vitordalvi.ucloan.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    // Função para extrair o username do usuário pelo token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Acessa as claims do usuário
    }

    // Extrai uma claim específica
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Função para retornar o token já criado
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Função para criar o token
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    // Construção do token
    private String buildToken(
            Map<String, Object> extraClaims, // Claims extras do token
            UserDetails userDetails, // userDetails para conseguirmos adicionar o username (email) do usuário no token
            long expiration // Tempo de expiração do token
    ) {
        return Jwts
                .builder() // Inicia a configuração do token
                .claims(extraClaims) // Adiciona as claims no token
                .subject(userDetails.getUsername()) // Adiciona o username do usuário (que tamo usando o email)
                .issuedAt(new Date(System.currentTimeMillis())) // Adiciona o momento em que o token foi emitido
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Adiciona o tempo de expiração
                .signWith(getSignInKey()) // Assina o token com a key criptografada (para verificar se o token é valido depois)
                .compact(); // Serializa o token em string
    }

    // Retorna se o token é valido, pelo nome do usuário e regra de expiração
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Retorna se o token está ou não expirado, vendo se a expiração já passou
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Retorna a Expiration do Token pela claim
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai todos os dados enviados no token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser() // Inicia a configuração do parser de tokens
                .verifyWith(getSignInKey()) // Valida o token com a key criptografada do sistema
                .build() // Finaliza a configuração e monta o parser
                .parseSignedClaims(token) // Parsing e verifica a assinatura, retornando as claims
                .getPayload(); // Retorna a payload do token
    }

    // Decodifica a key do sistema para validações
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

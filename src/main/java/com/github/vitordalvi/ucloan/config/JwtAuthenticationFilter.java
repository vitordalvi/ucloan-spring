package com.github.vitordalvi.ucloan.config;

import com.github.vitordalvi.ucloan.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenRepository tokenRepository) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    // Filtro interno para autorização do Spring Security
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Se a requisição tiver o path de autenticação, pode skippar esse filtro
        if (request.getServletPath().startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Variáveis para armazenar os dados do header
        final String authHeader = request.getHeader("Authorization"); // Pegar o header da requisição
        final String jwt; // Armazenar o value do header Authorization
        final String userEmail; // Armazenar o email

        // Se o header for nulo ou o value de Authorization não começar com Bearer
        // -> Pula o filtro, Spring Security vai negar o endpoint caso a requisição
        // não passe pelos outros filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Seta o JWT, cortando a string até o 7o caractere
        // ("Bearer ") <- 7 caracteres, então vamo extrair só o token
        jwt = authHeader.substring(7);

        // Seta o email do usuário (que foi utilizado como metodo de login)
        // extraindo ele pelas claims do token
        userEmail = jwtService.extractUsername(jwt);

        // Se o e-mail foi setado e o usuário não está autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extrai os dados do usuário
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            /*
            POSSIVELMENTE ESSA VALIDAÇÃO AQUI PODE TER MUDANÇAS NO FUTURO, CONFORME
            OS MEUS ESTUDOS, PORQUE SE O SISTEMA PRECISASSE ESCALAR, PRECISARIA
            COLOCAR UMA FORMA DE PERSISTÊNCIA MELHOR DESSE TOKEN, PORQUE OS TOKENS
            PRECISAM SER PESQUISADOS NO BANCO, TALVEZ SE USASSE CACHE (UM REDIS DA VIDA)
            TERIAMOS UMA PERFORMANCE MELHOR NO SISTEMA. ESSE TOKEN DEVERIA SER SALVO NESSE CACHE
            */

            // Valida o token, buscando o token no banco
            var isTokenValid = tokenRepository.findByToken(jwt)
                    // VALIDAÇÃO DOS ATRIBUTOS DO TOKEN, SE ESTÁ REVOGADO OU EXPIRADO
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    // SE PASSAR PELAS REGRAS DE EXPIRADO E REVOGADO (PELO O QUE ESTÁ NO BANCO)
                    // isTokenValid == true, SE NÃO, isTokanValid == false
                    .orElse(false);

            // SE O TOKEN FOR VALIDO, PASSA PELA REGRA DE NEGOCIO
            // (Verificar se o usuário é o mesmo e se não está expirado)
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                // Cria o token de autenticação ao usuário em memória
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // Carrega informações do usuário
                        null, // Sem credentials (a gente já sabe que o usuário é valido)
                        userDetails.getAuthorities() // Seta as permissões que o usuário tem
                );
                // Adiciona as informações de autenticação ao token
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // Seta a autenticação do usuário em memória
            }
        }

        // Passa para o próximo filtro do Spring Security
        filterChain.doFilter(request, response);
    }
}

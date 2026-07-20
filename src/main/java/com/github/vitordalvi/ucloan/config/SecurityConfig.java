package com.github.vitordalvi.ucloan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.github.vitordalvi.ucloan.entities.enums.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Paths públicos, acessíveis sem autenticação
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**", // Endpoint de autenticação
            "/v3/api-docs/**", // Swagger
            "/swagger-ui/**", // Swagger
            "/swagger-ui.html"// Swagger
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider,
                          LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    // Filtro de segurança principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desativado porque estamos usando JWT e não cookies para autenticação
                .authorizeHttpRequests( // Endpoints autorizados
                        req -> req.requestMatchers(WHITE_LIST_URL) // Endpoints publicos
                                .permitAll() // Autorização para usuários não autenticados acessarem esses endpoints
                                // Endpoints /admin só quem tem o cargo de gerenciador ou relativo
                                .requestMatchers(
                                        "/api/v1/equipments/admin/**",
                                        "/api/v1/equipment-models/admin/**",
                                        "/api/v1/users/admin/**",
                                        "/api/v1/loans/admin/**")
                                .hasAnyRole(ADMIN.name())
                                .anyRequest() // Qualquer endpoint válido
                                .authenticated() // Se estiver autenticado
                )

                // Criação da politica de sessão dos usuários
                .sessionManagement(session ->
                        // Sem criação de sessão, já que estamos usando JWT (comunicacao pelo header)
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) // Validador de credenciais
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Filtro de autenticação JWT
                // Endpoint padrão de logout
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                // Se sucesso
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()) // Limpa o contexto do usuário em memória da aplicação
                );

        return http.build();
    }
}

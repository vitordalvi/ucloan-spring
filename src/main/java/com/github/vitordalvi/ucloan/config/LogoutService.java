package com.github.vitordalvi.ucloan.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    /*
     * Com a arquitetura de JWT stateless, o servidor não mantém estado dos tokens
     * em banco de dados. O logout limpa o contexto de segurança da requisição e o
     * cliente descarta os tokens (access token e refresh token) do seu armazenamento local.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        SecurityContextHolder.clearContext();
    }
}

package com.github.vitordalvi.ucloan.config;

import com.github.vitordalvi.ucloan.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /* Função de logout (vai usado como configuração de logout padrão do SecurityConfig)

    Acho que essa aplicação da autenticação é boa pro contexto aprender e afins,
    mas como eu coloquei na classe do AuthFilter, tornar o JWT Stateful
    (Guardar o estado dele no banco e etc) pode não ser muito escalável e perde
    a funcionalidade propriedade "Stateless" do JWT, mas pelo menos garante
    que se um usuário realmente fizer login, esse ele não poderá logar com o mesmo
    token, assim não podendo "falsificar" o login
    */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        // Armazena o value de Authorization do header
        final String authHeader = request.getHeader("Authorization");
        final String jwt; // Armazena o token jwt

        // Se o authHeader for nulo ou não começar com "Bearer ", termina a função
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        // Corta a string até o 7o digito (onde começa o token)
        jwt = authHeader.substring(7);

        // Procura os tokens de todos os tipos no banco, se achar salva na variável
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null); // Se não achar, retorna como nulo

        // Se tem tokens salvos ->
        if (storedToken != null) {
            var userTokens = tokenRepository.findAllValidTokenByUser(storedToken.getUser().getId()); // Armazena os tokens do usuário
            userTokens.forEach(t -> { // Para cada token encontrado (independente do tipo ->
                t.setExpired(true); // Seta como expirado
                t.setRevoked(true); // Seta como revogado
            });

            tokenRepository.saveAll(userTokens); // Atualiza o estado dos tokens do usuário
            SecurityContextHolder.clearContext(); // Limpa a autenticação do usuário
        }
    }
}

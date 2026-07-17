package com.github.vitordalvi.ucloan.auditing;

import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationAuditAware implements AuditorAware<ApplicationUser> {

    // Responsável pela atualização dos campos auditáveis da AuditableBaseEntity
    @Override
    public Optional<ApplicationUser> getCurrentAuditor() {
        // Salva a autenticação que foi feita
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Se a autenticação não existir, ou se for uma autenticação anônima, retorna nulo
        if (authentication == null ||
        !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        // Retorna o objeto do usuário que foi pego autenticação
        ApplicationUser userPrincipal = (ApplicationUser) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal);
    }
}

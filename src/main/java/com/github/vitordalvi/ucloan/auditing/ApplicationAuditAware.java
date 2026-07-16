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
    @Override
    public Optional<ApplicationUser> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null ||
        !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        ApplicationUser userPrincipal = (ApplicationUser) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal);
    }
}

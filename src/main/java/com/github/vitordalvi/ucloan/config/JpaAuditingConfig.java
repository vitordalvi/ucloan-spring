package com.github.vitordalvi.ucloan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "applicationAuditAware") // Referência da AuditAware para criação da bean
public class JpaAuditingConfig {
}

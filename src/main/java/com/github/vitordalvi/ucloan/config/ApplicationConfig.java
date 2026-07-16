package com.github.vitordalvi.ucloan.config;

import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.vitordalvi.ucloan.entities.enums.Role;


@Configuration
public class ApplicationConfig {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationConfig(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> applicationUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner seedAdmin(ApplicationUserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@ucloan.com").isEmpty()) {
                ApplicationUser admin = new ApplicationUser();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail("admin@ucloan.com");
                admin.setPassword(encoder.encode("123456"));
                admin.setRole(Role.ADMIN);
                repo.save(admin);
            }
        };
    }
}

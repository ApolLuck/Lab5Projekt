package com.example.lab2projekt.domain.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Bean
    public AuditorAware<String> auditorAware() {
        System.out.println("AuditorAware bean zainicjalizowany");
        return () -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                var login = ((User) auth.getPrincipal()).getUsername();
                logger.info("Pobrano dane użytkownika: {}", login);
                return Optional.of(login);
            }
            logger.warn("Brak zalogowanego użytkownika!");
            return Optional.empty();
        };
    }
}


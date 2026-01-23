package com.example.notaFraisBackend.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and() // ← AJOUTEZ CETTE LIGNE POUR ACTIVER CORS
                .authorizeRequests()
                .requestMatchers("/api/utilisateurs/**").permitAll()
                .requestMatchers("/api/direction/**").permitAll()
                .requestMatchers("/api/services/**").permitAll() // ← AJOUTÉ
                .requestMatchers("/api/sections/**").permitAll() // ← AJOUTÉ
                .requestMatchers("/api/evaluation/**").permitAll() // ← AJOUTÉ
                .requestMatchers("/api/agents/**").permitAll()   // ← AJOUTÉ
                .requestMatchers("/api/annee-exercice/**").permitAll()   // ← AJOUTÉ
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }
}

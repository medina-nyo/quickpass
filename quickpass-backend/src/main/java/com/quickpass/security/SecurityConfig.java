package com.quickpass.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration de la sécurité Spring Boot.
 *
 * <p>
 * Gère la sécurité de l'application :
 * <ul>
 *   <li>Autorise certaines routes publiques (signup, activation, erreurs).</li>
 *   <li>Désactive CSRF (API stateless).</li>
 *   <li>Active CORS pour autoriser les appels du frontend Angular.</li>
 *   <li>Définit un encodeur de mots de passe conforme ANSSI (Argon2id).</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuration principale de la chaîne de filtres de sécurité.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation du CSRF (API REST stateless)
                .csrf(csrf -> csrf.disable())

                // Activation du CORS avec configuration personnalisée
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Définition des routes publiques
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/signup/**",
                                "/api/v1/activation/**",
                                "/error",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .httpBasic(httpBasic -> {});

        return http.build();
    }

    /**
     * Configure les origines autorisées pour les requêtes CORS (Angular localhost).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200")); // Frontend Angular
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Encodeur de mot de passe Argon2id conforme aux recommandations de l’ANSSI.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        int saltLength = 16;
        int hashLength = 32;
        int parallelism = 1;
        int memory = 65536;
        int iterations = 3;     

        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }
}

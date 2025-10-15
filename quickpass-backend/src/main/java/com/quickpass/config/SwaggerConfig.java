package com.quickpass.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI (Swagger UI) pour la documentation de l’API QuickPass.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Définit la configuration principale d’OpenAPI (titre, contact, serveur, licence).
     */
    @Bean
    public OpenAPI quickPassOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("QuickPass API - Module d’inscription")
                        .description("Documentation interactive des endpoints publics de QuickPass.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QuickPass Support")
                                .email("support@quickpass.com")
                                .url("https://quickpass.com"))
                        .license(new License()
                                .name("Propriétaire QuickPass © 2025")
                                .url("https://quickpass.com/licence")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Environnement local"),
                        new Server().url("https://api.quickpass.com").description("Production")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation complète du projet QuickPass")
                        .url("https://docs.quickpass.com"));
    }

    /**
     * Groupe OpenAPI pour le module d’inscription.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("signup")
                .pathsToMatch("/api/v1/signup/**")
                .build();
    }
}

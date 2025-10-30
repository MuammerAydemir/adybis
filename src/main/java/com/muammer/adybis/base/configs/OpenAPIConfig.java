package com.muammer.adybis.base.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {
        private String title = "API Documentation";
        private String version = "0.0.1";
        private String summary = "This is demo";
        private License license = new License().name("GNU GENERAL PUBLIC LICENSE\nversion 3")
                        .url("https://www.gnu.org/licenses/gpl-3.0-standalone.html");

        @Bean
        OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info().title(title).version(version).summary(summary).license(license))
                                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("BearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }

}

package com.sus.atende.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SUS Atende - API de Triagem e Direcionamento")
                        .description("API REST para triagem inteligente de pacientes e direcionamento para unidades de saúde apropriadas do SUS")
                        .version("1.0.0-MVP")
                        .contact(new Contact()
                                .name("Equipe SUS Atende")
                                .url("https://github.com/leonartlima/sus-atende")));
    }
}


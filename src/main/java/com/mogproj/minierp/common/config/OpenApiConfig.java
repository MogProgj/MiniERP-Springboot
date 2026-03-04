package com.mogproj.minierp.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI miniErpOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MiniERP API")
                        .description("Mini ERP system REST API — customers, products, orders, inventory")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MogProj")
                                .url("https://github.com/MogProgj/MiniERP-Springboot")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide a JWT token obtained from POST /auth/login")));
    }
}

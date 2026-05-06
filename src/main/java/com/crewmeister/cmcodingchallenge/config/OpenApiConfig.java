package com.crewmeister.cmcodingchallenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for the Crewmeister FX Rate Service.
 * Provides comprehensive API documentation with examples and error responses.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Crewmeister FX Rate Service API")
                        .description("A Spring Boot microservice that provides foreign exchange rate information with data sourced from the German Bundesbank API.\n\n" +
                                "**API Version:** v1\n" +
                                "**Base Path:** `/api/v1`\n\n" +
                                "This API provides currency exchange rate data and conversion services. All endpoints are versioned under `/api/v1/` for future compatibility.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Crewmeister Development Team")
                                .email("dev@crewmeister.com")
                                .url("https://crewmeister.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.crewmeister.com")
                                .description("Production server")
                ));
    }
}

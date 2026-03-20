package com.kauanferreira.smartorder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the SmartOrder API.
 *
 * <p>Configures the API documentation metadata displayed in the
 * Swagger UI, including title, description, version, contact
 * information, and license details.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI specification bean.
     *
     * @return the customized {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartOrder API")
                        .description("E-commerce backend API for managing categories, products, users, addresses, orders, and order items. Built with Spring Boot 4, JPA/Hibernate 7, PostgreSQL, and Flyway.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Kauan Santos Ferreira")
                                .email("kauanferreira3011@gmail.com")
                                .url("https://github.com/Kauan-FR"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

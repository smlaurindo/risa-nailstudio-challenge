package com.smlaurindo.risanailstudio.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.smlaurindo.risanailstudio.shared.constant.AuthConstants.REFRESH_TOKEN_COOKIE_NAME;

@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private int port;

    @Value("${app.env.is-demo}")
    private boolean isInDemo;

    @Bean
    public OpenAPI risaNailStudioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Risa Nail Studio API")
                        .description("""
                                RESTful API for managing appointments at a nail salon.
                                
                                ## Features
                                
                                ### Authentication
                                - Customer registration and login
                                - JWT authentication (Access Token and Refresh Token)
                                - Tokens managed via HTTP-only cookies
                                - Logout and token invalidation
                                
                                ### Services
                                - List available services
                                - Create new services (administrators only)
                                - Query service details
                                
                                ### Appointments
                                - Create appointments (customers)
                                - List appointments with filters (administrators)
                                - Query appointment details (administrators)
                                - Confirm appointments (administrators)
                                - Cancel appointments (administrators)
                                
                                ## Authentication and Authorization
                                
                                The API uses JWT (JSON Web Tokens) for authentication:
                                - **Access Token**: Short-lived token for request authentication
                                - **Refresh Token**: Long-lived token for Access Token renewal
                                
                                Tokens are stored in HTTP-only cookies for enhanced security.
                                
                                ### Roles
                                - **CUSTOMER**: Customer users who can schedule services
                                - **ADMIN**: Administrators who manage services and appointments
                                
                                ## Error Handling
                                
                                All errors follow a consistent pattern:
                                
                                - **400 Bad Request**: Validation errors
                                - **401 Unauthorized**: Authentication failure
                                - **403 Forbidden**: Insufficient permissions
                                - **404 Not Found**: Resource not found
                                - **409 Conflict**: Data conflict (e.g., email already registered)
                                - **422 Unprocessable Entity**: Business rule violation
                                - **500 Internal Server Error**: Internal server error
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Samuel Laurindo")
                                .email("contato.samuellaurindo@gmail.com")
                                .url("https://github.com/smlaurindo"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + port)
                                .description(isInDemo ? "Demonstration Server" : "Development Server")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("accessTokenCookieAuth"))
                .addSecurityItem(new SecurityRequirement().addList("refreshTokenCookieAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Access Token in Authorization header"))
                        .addSecuritySchemes("accessTokenCookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name(ACCESS_TOKEN_COOKIE_NAME)
                                .description("JWT Access Token in HTTP-only cookie"))
                        .addSecuritySchemes("refreshTokenCookieAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name(REFRESH_TOKEN_COOKIE_NAME)
                                        .description("Refresh Token in HTTP-only cookie"))
                        );
    }
}

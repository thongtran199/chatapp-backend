package com.springboot.chatapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot ChatApp APIs",
                version = "1",
                description = "Spring Boot ChatApp APIs Documentation",
                contact = @Contact(name = "Thong Van Tran", email = "thongtranr27@gmail.com")
        ),
        security = {
                @SecurityRequirement(name = "Authorization")
        }
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi chatAppApi() {
        return GroupedOpenApi.builder()
                .group("chat-app")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public SecurityScheme apiKey() {
        return new SecurityScheme()
                .type(Type.HTTP)
                .scheme("bearer")
                .in(In.HEADER)
                .name("Authorization")
                .description("JWT Bearer token for authorization");
    }
}

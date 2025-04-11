package com.example.UserService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI userApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("API documentation for the User microservice")
                        .version("1.0"));
    }
}


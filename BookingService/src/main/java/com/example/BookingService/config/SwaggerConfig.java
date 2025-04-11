package com.example.BookingService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI bookingApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking Service API")
                        .description("API documentation for the Booking microservice")
                        .version("1.0"));
    }
}


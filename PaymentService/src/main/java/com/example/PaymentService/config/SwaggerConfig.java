package com.example.PaymentService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI paymentApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service API")
                        .description("API documentation for the Payment microservice")
                        .version("1.0"));
    }
}

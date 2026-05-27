package io.github.sundenjaeger.amylensbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI amylensOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Amylens Server API")
                        .description(
                                "REST API for the AmyLens server (Module 3). " +
                                        "Consumed by Module 1 (device auth), Module 2 (session submission), and Module 4 (dashboard). " +
                                        "All dashboard endpoints require a valid login session via POST /login."
                        )
                        .version("1.0.0")
                );
    }
}

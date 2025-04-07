package com.goormthonuniv.backend.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("구름톤 유니브 API")
                        .description("백엔드 고급반 Swagger 문서")
                        .version("v1.0.0"));
    }
}

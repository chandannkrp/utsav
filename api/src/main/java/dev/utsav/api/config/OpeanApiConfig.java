package dev.utsav.api.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpeanApiConfig {


    @Bean
    public OpenAPI utsavOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                .title("Utsav API")
                .description("API documentation for Utsav application")
                .version("1.0.0"));
    }
}

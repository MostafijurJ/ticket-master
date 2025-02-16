package com.learn.ms.payment.presenter.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = {"springdoc.swagger-ui.enabled"}, matchIfMissing = true)
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .version("0.1")
                        .description("This service is responsible for all the payment related operations.")
                        .contact(new Contact()
                                .name("Mostafijur Rahman")
                                .email("mostafijurj@gmail.com"))
                        .license(new License()
                                .name("Apache License, Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:1155")
                                .description("Local Dev Server")
                ));
    }
}

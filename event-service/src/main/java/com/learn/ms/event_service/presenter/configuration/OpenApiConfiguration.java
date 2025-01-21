package com.learn.ms.event_service.presenter.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = {"springdoc.swagger-ui.enabled"}, matchIfMissing = true)
public class OpenApiConfiguration {
    private static final String userApplicationName = "event-service";

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI openAPI() {
        final var serviceName = applicationName;
        final var info = new Info().title(serviceName.toUpperCase()).description(serviceName.toUpperCase()).version("1.0.0");

        final var wellKnownForGateway = String.format("/%s/api/v1/user/dev-test/well-known", userApplicationName);
        final var securitySchemeForGateway = new SecurityScheme().description("schema").type(SecurityScheme.Type.OPENIDCONNECT).openIdConnectUrl(wellKnownForGateway);

        final var components = new Components()
                .addSecuritySchemes(SecurityScheme.Type.OPENIDCONNECT.name(), securitySchemeForGateway)
                .addSecuritySchemes(SecurityScheme.Type.APIKEY.name(), new SecurityScheme().name("CurrentContext").in(SecurityScheme.In.HEADER).type(SecurityScheme.Type.APIKEY));

        final var securityItemUserContext = new SecurityRequirement().addList(SecurityScheme.Type.APIKEY.name());
        final var securityItemForGateway = new SecurityRequirement().addList(SecurityScheme.Type.OPENIDCONNECT.name());

        return new OpenAPI()
                .info(info)
                .servers(List.of(new Server().url("/"), new Server().url("/".concat(applicationName))))
                .security(List.of(securityItemForGateway, securityItemUserContext))
                .components(components);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            final var stream = openApi.getPaths()
                    .values()
                    .stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream());
            final Schema<?> schema = new StringSchema().type("string")._enum(List.of("EN", "BN"));
            stream.forEach(operation -> operation.addParametersItem(new HeaderParameter().name("Accept-Language").in("header").required(true).schema(schema)));
        };
    }
}

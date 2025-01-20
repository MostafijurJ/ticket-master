package com.learn.ms.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route to Event Service
                .route("event-service-route", r -> r.path("/event/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://event-service"))

                // Route to Search Service
                .route("search-service-route", r -> r.path("/search/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://search-service"))
                .build();
    }
}
package com.learn.ms.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> {
            String clientIp = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Forwarded-For");
            if (clientIp == null) {
                clientIp = exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress();
            }
            return clientIp != null ? reactor.core.publisher.Mono.just(clientIp)
                    : reactor.core.publisher.Mono.empty();
        };
    }
}

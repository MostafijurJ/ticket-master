package com.learn.ms.event_service.presenter.configuration.feign;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ticket-service")
public class FeignProperty {
    private Map<String, FeignDetail> feign = new HashMap<>();

    public FeignDetail get(String key) {
        return feign.get(key);
    }

    @Data
    public static class FeignDetail {
        private String name;
        private String host;
        private String path;
        private String tokenUrl;
        private String refreshTokenUrl;
        private Response response = new Response();

        private MultiValueMap<String, String> param;
        private MultiValueMap<String, String> header;

        private Map<String, Object> applicationJson;
        private MultiValueMap<String, String> formUrlencoded;
    }

    @Data
    public static class Response {
        private Long expiry = 5L;
        private String expiryKey = "expires_in";
        private String token = "access_token";
    }
}

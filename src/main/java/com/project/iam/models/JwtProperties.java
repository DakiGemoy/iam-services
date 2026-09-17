package com.project.iam.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("service.iam")
public class JwtProperties{
    private Token token;

    @Data
    public static class Token{
        private String secret;
        private Long expiration;
    }
}

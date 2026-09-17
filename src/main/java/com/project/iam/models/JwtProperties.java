package com.project.iam.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("service.iam")
public class JwtProperties{
    private Token token;
    private Permission apiPermission;

    @Data
    public static class Token{
        private String secret;
        private Long expiration;
    }

    @Data
    public static class Permission{
        private List<String> manager;
        private List<String> admin;
        private List<String> user;
    }
}

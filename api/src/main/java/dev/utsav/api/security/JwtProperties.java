package dev.utsav.api.security;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "utsav.security.jwt")
public record JwtProperties(
        String secret,
        long expirationMs
) {
}

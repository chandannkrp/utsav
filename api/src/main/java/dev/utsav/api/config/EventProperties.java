package dev.utsav.api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "utsav.events")
public record EventProperties(
        int defaultLimit,
        int maxLimit
) {

    public EventProperties{
        if(defaultLimit <= 0) defaultLimit = 20;
        if(maxLimit <= 0) maxLimit = 100;
    }
}

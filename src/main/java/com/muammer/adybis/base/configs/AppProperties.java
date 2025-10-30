package com.muammer.adybis.base.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private String jwtSecretKey;
    private Duration jwtExpirationTime;
    private String baseUrl;

}

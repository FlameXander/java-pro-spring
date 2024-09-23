package ru.flamexander.transfer.service.limits.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "limits")
@Getter
@Setter
public class LimitsConfig {
    private Integer defaultLimit = 10000;
}

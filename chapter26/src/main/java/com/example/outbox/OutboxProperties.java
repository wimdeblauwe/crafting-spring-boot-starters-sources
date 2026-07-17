package com.example.outbox;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "outbox")
public record OutboxProperties(@DefaultValue("1s") Duration pollInterval,
                               @DefaultValue Publisher publisher,
                               @DefaultValue Health health) {

    public record Publisher(SecureString apiKey) {
    }

    public record Health(@DefaultValue("30s") Duration maxAge) {
    }
}

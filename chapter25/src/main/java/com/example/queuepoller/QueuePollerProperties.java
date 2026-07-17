package com.example.queuepoller;

import java.time.Duration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("queue-poller")
public record QueuePollerProperties(
        @NotBlank String queueName,
        @DefaultValue("50ms") @NotNull Duration receiveTimeout) {
}

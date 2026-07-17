package com.example.meilisearch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "meilisearch")
public record MeilisearchProperties(
        @DefaultValue("localhost") String host,
        @DefaultValue("7700") int port,
        String apiKey) {
}

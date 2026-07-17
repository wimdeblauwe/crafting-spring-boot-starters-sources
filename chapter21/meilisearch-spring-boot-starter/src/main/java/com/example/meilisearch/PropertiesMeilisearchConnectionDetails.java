package com.example.meilisearch;

import org.springframework.util.Assert;

class PropertiesMeilisearchConnectionDetails implements MeilisearchConnectionDetails {

    private final MeilisearchProperties properties;

    PropertiesMeilisearchConnectionDetails(MeilisearchProperties properties) {
        Assert.hasText(properties.host(), "Host must not be empty");
        Assert.isTrue(properties.port() > 0, "Port must be greater than 0");
        Assert.hasText(properties.apiKey(), "API key must not be empty");
        this.properties = properties;
    }

    @Override
    public String getHost() {
        return properties.host();
    }

    @Override
    public int getPort() {
        return properties.port();
    }

    @Override
    public String getApiKey() {
        return properties.apiKey();
    }
}

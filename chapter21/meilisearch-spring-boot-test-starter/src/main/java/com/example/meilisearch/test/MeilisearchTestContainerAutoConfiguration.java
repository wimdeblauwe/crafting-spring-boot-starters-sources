package com.example.meilisearch.test;

import com.example.meilisearch.MeilisearchAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = {MeilisearchAutoConfiguration.class, ServiceConnectionAutoConfiguration.class})
public class MeilisearchTestContainerAutoConfiguration {

    static final String DEFAULT_IMAGE = "getmeili/meilisearch:v1.10";

    @Bean
    @ServiceConnection
    MeilisearchContainer meilisearchContainer() {
        return new MeilisearchContainer(DEFAULT_IMAGE);
    }
}

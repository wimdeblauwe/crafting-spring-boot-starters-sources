package com.example.notesapp;

import com.example.meilisearch.test.MeilisearchContainer;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

// tag::config[]
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    MeilisearchContainer meilisearchContainer() {
        return new MeilisearchContainer("getmeili/meilisearch:v1.10");
    }
}
// end::config[]

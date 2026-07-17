package com.example.meilisearch;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(Client.class)
@EnableConfigurationProperties(MeilisearchProperties.class)
public class MeilisearchAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MeilisearchConnectionDetails.class)
    PropertiesMeilisearchConnectionDetails meilisearchConnectionDetails(MeilisearchProperties properties) {
        return new PropertiesMeilisearchConnectionDetails(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    Client meilisearchClient(MeilisearchConnectionDetails connectionDetails) {
        Config config = new Config(connectionDetails.getHostUrl(), connectionDetails.getApiKey());
        return new Client(config);
    }
}

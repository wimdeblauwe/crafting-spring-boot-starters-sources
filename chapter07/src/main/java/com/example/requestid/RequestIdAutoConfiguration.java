package com.example.requestid;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(RequestIdProperties.class)
public class RequestIdAutoConfiguration {

    @Bean
    public RequestIdGenerator requestIdGenerator(RequestIdProperties properties) {
        return new RequestIdGenerator(properties.generation());
    }
}

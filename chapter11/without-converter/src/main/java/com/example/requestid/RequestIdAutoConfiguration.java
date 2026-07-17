package com.example.requestid;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@EnableConfigurationProperties(RequestIdProperties.class)
@PropertySource("classpath:request-id-defaults.properties")
public class RequestIdAutoConfiguration {

    @Bean
    public RequestIdGenerator requestIdGenerator(RequestIdProperties properties) {
        return new RequestIdGenerator(properties.generation());
    }

    @Bean
    public HeaderNameResolver headerNameResolver(RequestIdProperties properties) {
        return new HeaderNameResolver(properties.pathStrategies(), properties.header().name());
    }
}

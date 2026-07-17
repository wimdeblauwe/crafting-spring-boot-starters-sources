package com.example.requestid;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;

@AutoConfiguration
@EnableConfigurationProperties(RequestIdProperties.class)
@PropertySource("classpath:request-id-defaults.properties")
public class RequestIdAutoConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public Converter<String, HeaderName> stringToHeaderNameConverter() {
        return new StringToHeaderNameConverter();
    }

    @Bean
    public RequestIdGenerator requestIdGenerator(RequestIdProperties properties) {
        return new RequestIdGenerator(properties.generation());
    }

    @Bean
    public HeaderNameResolver headerNameResolver(RequestIdProperties properties) {
        return new HeaderNameResolver(properties.pathStrategies(), properties.header().name());
    }
}

package com.example.requestid;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(RequestIdProperties.class)
public class RequestIdAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnNonEmptyMapProperty("request-id.header-overrides")
    public HeaderOverrideResolver headerOverrideResolver(RequestIdProperties properties) {
        return new HeaderOverrideResolver(properties.headerOverrides());
    }
}

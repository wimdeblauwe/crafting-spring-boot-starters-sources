package com.example.outbox.actuate;

import com.example.outbox.OutboxAutoConfiguration;
import com.example.outbox.OutboxDrainer;
import com.example.outbox.OutboxProperties;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.endpoint.SanitizingFunction;
import org.springframework.boot.health.autoconfigure.contributor.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

// tag::class[]
@AutoConfiguration(after = OutboxAutoConfiguration.class)
@ConditionalOnClass(HealthIndicator.class)
@ConditionalOnBean(OutboxDrainer.class)
public class OutboxActuatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "outboxHealthIndicator")
    @ConditionalOnEnabledHealthIndicator("outbox")
    public OutboxHealthIndicator outboxHealthIndicator(OutboxDrainer drainer,
                                                       OutboxProperties properties) {
        return new OutboxHealthIndicator(drainer, properties.health().maxAge());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint(endpoint = OutboxEndpoint.class)
    public OutboxEndpoint outboxEndpoint(OutboxDrainer drainer) {
        return new OutboxEndpoint(drainer);
    }

    // tag::sanitizer[]
    @Bean
    @ConditionalOnClass(SanitizingFunction.class)
    public SanitizingFunction outboxSanitizingFunction() {
        return SanitizingFunction.sanitizeValue()
                .ifKeyEquals("outbox.publisher.api-key");
    }
    // end::sanitizer[]
}
// end::class[]

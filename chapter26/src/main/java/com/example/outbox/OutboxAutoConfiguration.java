package com.example.outbox;

import java.time.Clock;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@EnableConfigurationProperties(OutboxProperties.class)
@ConditionalOnBean(OutboxPublisher.class)
public class OutboxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Clock outboxClock() {
        return Clock.systemUTC();
    }

    @Bean
    @ConditionalOnMissingBean
    public OutboxStore outboxStore(Clock outboxClock) {
        return new InMemoryOutboxStore(outboxClock);
    }

    @Bean
    @ConditionalOnMissingBean(name = "outboxThreadFactory")
    public ThreadFactory outboxThreadFactory(Environment environment) {
        boolean virtual = environment.getProperty("spring.threads.virtual.enabled", Boolean.class, false);
        return virtual
                ? Thread.ofVirtual().name("outbox-drainer-", 0).factory()
                : Thread.ofPlatform().daemon().name("outbox-drainer-", 0).factory();
    }

    @Bean
    public OutboxDrainer outboxDrainer(OutboxStore store,
                                       OutboxPublisher publisher,
                                       OutboxProperties properties,
                                       Clock outboxClock,
                                       @Qualifier("outboxThreadFactory") ThreadFactory threadFactory) {
        return new OutboxDrainer(store, publisher, properties.pollInterval(), outboxClock, threadFactory);
    }
}

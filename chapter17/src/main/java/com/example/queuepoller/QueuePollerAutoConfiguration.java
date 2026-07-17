package com.example.queuepoller;

import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@EnableConfigurationProperties(QueuePollerProperties.class)
@ConditionalOnBean(MessageHandler.class)
public class QueuePollerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InMemoryBroker inMemoryBroker() {
        return InMemoryBroker.INSTANCE;
    }

    @Bean
    @ConditionalOnMissingBean(name = "queuePollerThreadFactory")
    public ThreadFactory queuePollerThreadFactory(Environment environment) {
        boolean virtual = environment.getProperty("spring.threads.virtual.enabled", Boolean.class, false);
        return virtual
                ? Thread.ofVirtual().name("queue-poller-", 0).factory()
                : Thread.ofPlatform().daemon().name("queue-poller-", 0).factory();
    }

    @Bean
    public QueuePoller queuePoller(InMemoryBroker broker,
                                   MessageHandler handler,
                                   QueuePollerProperties properties,
                                   @Qualifier("queuePollerThreadFactory") ThreadFactory threadFactory) {
        return new QueuePoller(broker, handler, properties.queueName(), properties.receiveTimeout(), threadFactory);
    }
}

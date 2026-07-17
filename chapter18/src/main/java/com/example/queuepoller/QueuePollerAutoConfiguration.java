package com.example.queuepoller;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.ListableBeanFactory;
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
                                   ListableBeanFactory beanFactory, // <1>
                                   QueuePollerProperties properties,
                                   @Qualifier("queuePollerThreadFactory") ThreadFactory threadFactory) {
        MessageHandler handler = resolveHandler(beanFactory);
        return new QueuePoller(broker, handler, properties.queueName(),
                properties.receiveTimeout(), threadFactory);
    }

    private MessageHandler resolveHandler(ListableBeanFactory beanFactory) {
        String[] names = beanFactory.getBeanNamesForType(MessageHandler.class); // <2>
        if (names.length > 1) {
            throw new MultipleMessageHandlersException(Arrays.asList(names)); // <3>
        }
        return beanFactory.getBean(names[0], MessageHandler.class);
    }
}

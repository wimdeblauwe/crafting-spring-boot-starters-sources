package com.example.queuepoller;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
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
    @ConditionalOnMissingBean
    public DefaultQueuePollerObservationConvention defaultQueuePollerObservationConvention() {
        return new DefaultQueuePollerObservationConvention();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultMessageHandlerObservationConvention defaultMessageHandlerObservationConvention() {
        return new DefaultMessageHandlerObservationConvention();
    }

    @Bean
    public QueuePoller queuePoller(InMemoryBroker broker,
                                   ListableBeanFactory beanFactory,
                                   QueuePollerProperties properties,
                                   @Qualifier("queuePollerThreadFactory") ThreadFactory threadFactory,
                                   ObjectProvider<ObservationRegistry> observationRegistries,
                                   ObjectProvider<QueuePollerObservationConvention> conventions,
                                   DefaultQueuePollerObservationConvention defaultConvention) {
        MessageHandler handler = resolveHandler(beanFactory);
        if (properties.receiveTimeout().isZero() || properties.receiveTimeout().isNegative()) {
            throw new InvalidReceiveTimeoutException(properties.receiveTimeout());
        }
        // tag::convention-resolution[]
        ObservationRegistry registry = observationRegistries.getIfAvailable(() -> ObservationRegistry.NOOP);
        QueuePollerObservationConvention userConvention = conventions
                .stream()
                .filter(c -> !(c instanceof DefaultQueuePollerObservationConvention))
                .findFirst()
                .orElse(null);
        // end::convention-resolution[]
        return new QueuePoller(broker, handler, properties.queueName(),
                properties.receiveTimeout(), threadFactory,
                registry, userConvention, defaultConvention);
    }

    // tag::bpp-registration[]
    @Bean
    public static MessageHandlerObservationPostProcessor messageHandlerObservationPostProcessor(
            ObjectProvider<ObservationRegistry> observationRegistries,
            ObjectProvider<MessageHandlerObservationConvention> conventions) {
        return new MessageHandlerObservationPostProcessor(observationRegistries, conventions);
    }
    // end::bpp-registration[]

    private MessageHandler resolveHandler(ListableBeanFactory beanFactory) {
        String[] names = beanFactory.getBeanNamesForType(MessageHandler.class);
        if (names.length > 1) {
            throw new MultipleMessageHandlersException(Arrays.asList(names));
        }
        return beanFactory.getBean(names[0], MessageHandler.class);
    }
}

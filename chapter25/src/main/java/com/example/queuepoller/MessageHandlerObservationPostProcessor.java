package com.example.queuepoller;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MessageHandlerObservationPostProcessor implements BeanPostProcessor {

    private final ObjectProvider<ObservationRegistry> registries;
    private final ObjectProvider<MessageHandlerObservationConvention> conventions;

    public MessageHandlerObservationPostProcessor(
            ObjectProvider<ObservationRegistry> registries,
            ObjectProvider<MessageHandlerObservationConvention> conventions) {
        this.registries = registries;
        this.conventions = conventions;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ObservedMessageHandler) {
            return bean;
        }
        if (bean instanceof MessageHandler delegate) {
            ObservationRegistry registry = registries.getIfAvailable(() -> ObservationRegistry.NOOP);
            MessageHandlerObservationConvention userConvention = conventions
                    .stream()
                    .filter(c -> !(c instanceof DefaultMessageHandlerObservationConvention))
                    .findFirst()
                    .orElse(null);
            MessageHandlerObservationConvention defaultConvention = conventions
                    .stream()
                    .filter(DefaultMessageHandlerObservationConvention.class::isInstance)
                    .findFirst()
                    .orElseGet(DefaultMessageHandlerObservationConvention::new);
            return new ObservedMessageHandler(delegate, beanName, registry, userConvention, defaultConvention);
        }
        return bean;
    }
}

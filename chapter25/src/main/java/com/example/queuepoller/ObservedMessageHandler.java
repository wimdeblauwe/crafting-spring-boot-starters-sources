package com.example.queuepoller;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

public class ObservedMessageHandler implements MessageHandler {

    private final MessageHandler delegate;
    private final String beanName;
    private final ObservationRegistry observationRegistry;
    private final MessageHandlerObservationConvention userConvention;
    private final MessageHandlerObservationConvention defaultConvention;

    public ObservedMessageHandler(MessageHandler delegate,
                                  String beanName,
                                  ObservationRegistry observationRegistry,
                                  MessageHandlerObservationConvention userConvention,
                                  MessageHandlerObservationConvention defaultConvention) {
        this.delegate = delegate;
        this.beanName = beanName;
        this.observationRegistry = observationRegistry;
        this.userConvention = userConvention;
        this.defaultConvention = defaultConvention;
    }

    public MessageHandler getDelegate() {
        return delegate;
    }

    @Override
    public void handle(String message) {
        MessageHandlerContext context = new MessageHandlerContext(beanName);
        Observation observation = QueuePollerObservation.HANDLER_INVOCATION
                .observation(userConvention, defaultConvention, () -> context, observationRegistry)
                .start();
        try (Observation.Scope scope = observation.openScope()) {
            delegate.handle(message);
            context.setOutcome("success");
        } catch (Throwable t) {
            context.setOutcome("error");
            observation.error(t);
            throw t;
        } finally {
            observation.stop();
        }
    }
}

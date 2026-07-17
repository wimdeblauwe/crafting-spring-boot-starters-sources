package com.example.queuepoller;

import io.micrometer.common.KeyValues;

public class DefaultMessageHandlerObservationConvention implements MessageHandlerObservationConvention {

    @Override
    public String getName() {
        return "queue.handler.invocation";
    }

    @Override
    public String getContextualName(MessageHandlerContext context) {
        return "queue-poller handle " + context.getHandlerBeanName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(MessageHandlerContext context) {
        return KeyValues.of(
                QueuePollerObservation.HandlerLowCardinalityTags.HANDLER_BEAN_NAME.withValue(context.getHandlerBeanName()),
                QueuePollerObservation.HandlerLowCardinalityTags.OUTCOME.withValue(
                        context.getOutcome() == null ? "unknown" : context.getOutcome()));
    }
}

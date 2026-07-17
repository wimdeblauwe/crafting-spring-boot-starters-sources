package com.example.queuepoller;

import io.micrometer.common.KeyValues;

public class DefaultQueuePollerObservationConvention implements QueuePollerObservationConvention {

    @Override
    public String getName() {
        return "queue.poller.cycle";
    }

    @Override
    public String getContextualName(QueuePollerContext context) {
        return "queue-poller poll " + context.getQueueName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(QueuePollerContext context) {
        return KeyValues.of(
                QueuePollerObservation.LowCardinalityTags.QUEUE_NAME.withValue(context.getQueueName()),
                QueuePollerObservation.LowCardinalityTags.OUTCOME.withValue(
                        context.getOutcome() == null ? "unknown" : context.getOutcome()));
    }
}

package com.example.queuepoller;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

public interface QueuePollerObservationConvention extends ObservationConvention<QueuePollerContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof QueuePollerContext;
    }
}

package com.example.queuepoller;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

public interface MessageHandlerObservationConvention extends ObservationConvention<MessageHandlerContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof MessageHandlerContext;
    }
}

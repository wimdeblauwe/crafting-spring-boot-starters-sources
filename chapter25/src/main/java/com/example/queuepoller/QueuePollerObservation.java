package com.example.queuepoller;

import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;

public enum QueuePollerObservation implements ObservationDocumentation {

    POLL_CYCLE {
        @Override
        public Class<? extends ObservationConvention<? extends Observation.Context>> getDefaultConvention() {
            return DefaultQueuePollerObservationConvention.class;
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return LowCardinalityTags.values();
        }
    },

    HANDLER_INVOCATION {
        @Override
        public Class<? extends ObservationConvention<? extends Observation.Context>> getDefaultConvention() {
            return DefaultMessageHandlerObservationConvention.class;
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return HandlerLowCardinalityTags.values();
        }
    };

    public enum LowCardinalityTags implements KeyName {

        QUEUE_NAME {
            @Override
            public String asString() {
                return "queue.name";
            }
        },

        OUTCOME {
            @Override
            public String asString() {
                return "queue.poll.outcome";
            }
        }
    }

    public enum HandlerLowCardinalityTags implements KeyName {

        HANDLER_BEAN_NAME {
            @Override
            public String asString() {
                return "queue.handler.bean";
            }
        },

        OUTCOME {
            @Override
            public String asString() {
                return "queue.handler.outcome";
            }
        }
    }
}

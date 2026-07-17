package com.example.queuepoller;

import io.micrometer.observation.Observation;

public class MessageHandlerContext extends Observation.Context {

    private final String handlerBeanName;
    private String outcome;

    public MessageHandlerContext(String handlerBeanName) {
        this.handlerBeanName = handlerBeanName;
    }

    public String getHandlerBeanName() {
        return handlerBeanName;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}

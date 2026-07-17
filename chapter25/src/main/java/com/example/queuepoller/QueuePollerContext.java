package com.example.queuepoller;

import io.micrometer.observation.Observation;

public class QueuePollerContext extends Observation.Context {

    private final String queueName;
    private String outcome;

    public QueuePollerContext(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}

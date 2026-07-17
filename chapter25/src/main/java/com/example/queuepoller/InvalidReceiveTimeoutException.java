package com.example.queuepoller;

import java.time.Duration;

public class InvalidReceiveTimeoutException extends QueuePollerStartupException {

    private final Duration value;

    public InvalidReceiveTimeoutException(Duration value) {
        super("queue-poller.receive-timeout must be a positive duration, was %s".formatted(value));
        this.value = value;
    }

    public Duration value() {
        return value;
    }
}

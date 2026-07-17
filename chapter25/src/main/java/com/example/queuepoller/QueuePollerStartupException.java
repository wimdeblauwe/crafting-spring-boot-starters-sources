package com.example.queuepoller;

public abstract class QueuePollerStartupException extends RuntimeException {

    protected QueuePollerStartupException(String message) {
        super(message);
    }
}

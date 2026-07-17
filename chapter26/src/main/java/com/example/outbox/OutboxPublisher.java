package com.example.outbox;

public interface OutboxPublisher {

    void publish(OutboxMessage message) throws Exception;
}

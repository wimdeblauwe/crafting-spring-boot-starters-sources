package com.example.outbox;

import java.time.Instant;
import java.util.UUID;

import org.springframework.util.Assert;

public record OutboxMessage(UUID id,
                            Instant createdAt,
                            String destination,
                            String payload) {

    public OutboxMessage {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(createdAt, "createdAt must not be null");
        Assert.hasText(destination, "destination must not be blank");
        Assert.notNull(payload, "payload must not be null");
    }
}

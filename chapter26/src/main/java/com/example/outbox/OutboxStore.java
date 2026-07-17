package com.example.outbox;

import java.util.List;
import java.util.UUID;

public interface OutboxStore {

    OutboxMessage append(String destination, String payload);

    /**
     * Returns the pending messages in creation order, oldest first.
     */
    List<OutboxMessage> pending();

    void delete(UUID id);
}

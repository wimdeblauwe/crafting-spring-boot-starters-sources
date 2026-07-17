package com.example.outbox;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOutboxStore implements OutboxStore {

    private static final Comparator<OutboxMessage> CREATION_ORDER =
            Comparator.comparing(OutboxMessage::createdAt).thenComparing(OutboxMessage::id);

    private final Map<UUID, OutboxMessage> pending = new ConcurrentHashMap<>();
    private final Clock clock;

    public InMemoryOutboxStore(Clock clock) {
        this.clock = clock;
    }

    @Override
    public OutboxMessage append(String destination, String payload) {
        UUID id = UUID.randomUUID();
        OutboxMessage message = new OutboxMessage(id, clock.instant(), destination, payload);
        pending.put(id, message);
        return message;
    }

    @Override
    public List<OutboxMessage> pending() {
        return pending.values().stream()
                .sorted(CREATION_ORDER)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        pending.remove(id);
    }
}

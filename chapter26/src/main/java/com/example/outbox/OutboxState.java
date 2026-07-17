package com.example.outbox;

import java.time.Duration;
import java.time.Instant;

public record OutboxState(int pendingCount,
                          Duration oldestUnsentAge,
                          Instant oldestUnsentAt,
                          Instant lastDrainAt,
                          boolean draining) {
}

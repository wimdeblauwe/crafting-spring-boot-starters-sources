package com.example.outbox.actuate;

import java.time.Duration;

import com.example.outbox.OutboxDrainer;
import com.example.outbox.OutboxState;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;

public class OutboxHealthIndicator implements HealthIndicator {

    private final OutboxDrainer drainer;
    private final Duration maxAge;

    public OutboxHealthIndicator(OutboxDrainer drainer, Duration maxAge) {
        this.drainer = drainer;
        this.maxAge = maxAge;
    }

    @Override
    public Health health() {
        OutboxState state = drainer.state();
        Health.Builder builder = state.oldestUnsentAge().compareTo(maxAge) > 0 ? Health.down() : Health.up();
        return builder
                .withDetail("pendingCount", state.pendingCount())
                .withDetail("oldestUnsentAge", state.oldestUnsentAge().toString())
                .withDetail("maxAge", maxAge.toString())
                .build();
    }
}

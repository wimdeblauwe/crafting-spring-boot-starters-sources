package com.example.outbox;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryOutboxStoreTest {

    @Test
    void pendingReturnsMessagesInCreationOrder() {
        TickingClock clock = new TickingClock(Instant.parse("2026-01-01T10:00:00Z"));
        InMemoryOutboxStore store = new InMemoryOutboxStore(clock);

        List<String> appended = IntStream.range(0, 50)
                .mapToObj(i -> store.append("orders", "payload-" + i).payload())
                .toList();

        assertThat(store.pending())
                .extracting(OutboxMessage::payload)
                .containsExactlyElementsOf(appended);
    }

    @Test
    void firstPendingMessageIsTheOldest() {
        TickingClock clock = new TickingClock(Instant.parse("2026-01-01T10:00:00Z"));
        InMemoryOutboxStore store = new InMemoryOutboxStore(clock);

        Instant firstCreatedAt = store.append("orders", "payload-0").createdAt();
        IntStream.range(1, 50).forEach(i -> store.append("orders", "payload-" + i));

        assertThat(store.pending().getFirst().createdAt()).isEqualTo(firstCreatedAt);
    }

    private static final class TickingClock extends Clock {

        private Instant current;

        private TickingClock(Instant start) {
            this.current = start;
        }

        @Override
        public Instant instant() {
            Instant result = current;
            current = current.plus(Duration.ofSeconds(1));
            return result;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }
    }
}

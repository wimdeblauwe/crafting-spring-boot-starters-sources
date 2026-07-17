package com.example.outbox;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

public class OutboxDrainer implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(OutboxDrainer.class);

    private final OutboxStore store;
    private final OutboxPublisher publisher;
    private final Duration pollInterval;
    private final Clock clock;
    private final ThreadFactory threadFactory;

    private final Object lifecycleLock = new Object();
    private volatile boolean running;
    private volatile boolean draining;
    private volatile Instant lastDrainAt;
    private Thread worker;
    private CountDownLatch stopped;

    public OutboxDrainer(OutboxStore store,
                         OutboxPublisher publisher,
                         Duration pollInterval,
                         Clock clock,
                         ThreadFactory threadFactory) {
        this.store = store;
        this.publisher = publisher;
        this.pollInterval = pollInterval;
        this.clock = clock;
        this.threadFactory = threadFactory;
    }

    @Override
    public void start() {
        synchronized (lifecycleLock) {
            if (running) {
                return;
            }
            running = true;
            stopped = new CountDownLatch(1);
            worker = threadFactory.newThread(this::loop);
            worker.setName("outbox-drainer");
            worker.start();
            logger.info("Started outbox drainer (poll interval {})", pollInterval);
        }
    }

    @Override
    public void stop() {
        Thread toJoin;
        CountDownLatch toAwait;
        synchronized (lifecycleLock) {
            if (!running) {
                return;
            }
            running = false;
            toJoin = worker;
            toAwait = stopped;
            if (toJoin != null) {
                toJoin.interrupt();
            }
        }
        try {
            if (!toAwait.await(5, TimeUnit.SECONDS)) {
                logger.warn("Outbox drainer did not stop within 5s");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        logger.info("Stopped outbox drainer");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return DEFAULT_PHASE;
    }

    // tag::state[]
    public OutboxState state() {
        List<OutboxMessage> pending = store.pending();
        OutboxMessage oldest = pending.isEmpty() ? null : pending.getFirst();
        Instant oldestAt = oldest == null ? null : oldest.createdAt();
        Duration oldestAge = oldestAt == null ? Duration.ZERO : Duration.between(oldestAt, clock.instant());
        return new OutboxState(pending.size(), oldestAge, oldestAt, lastDrainAt, draining);
    }
    // end::state[]

    private void loop() {
        try {
            while (running) {
                try {
                    drainOnce();
                    Thread.sleep(pollInterval.toMillis());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } finally {
            running = false;
            stopped.countDown();
        }
    }

    private void drainOnce() {
        draining = true;
        try {
            for (OutboxMessage message : store.pending()) {
                if (!running) {
                    return;
                }
                try {
                    publisher.publish(message);
                    store.delete(message.id());
                } catch (Exception ex) {
                    logger.warn("Publisher failed for message {} to {}: {}", message.id(), message.destination(), ex.toString());
                    break;
                }
            }
        } finally {
            lastDrainAt = clock.instant();
            draining = false;
        }
    }
}

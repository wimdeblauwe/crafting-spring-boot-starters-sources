package com.example.queuepoller;

import java.time.Duration;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public final class InMemoryBroker {

    public static final InMemoryBroker INSTANCE = new InMemoryBroker();

    private final ConcurrentHashMap<String, BlockingDeque<String>> queues = new ConcurrentHashMap<>();

    private InMemoryBroker() {
    }

    public void send(String queue, String message) {
        queueFor(queue).add(message);
    }

    public String poll(String queue, Duration timeout) throws InterruptedException {
        return queueFor(queue).poll(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    public int depth(String queue) {
        return queueFor(queue).size();
    }

    public void clear() {
        queues.values().forEach(BlockingDeque::clear);
    }

    private BlockingDeque<String> queueFor(String name) {
        return queues.computeIfAbsent(name, key -> new LinkedBlockingDeque<>());
    }
}

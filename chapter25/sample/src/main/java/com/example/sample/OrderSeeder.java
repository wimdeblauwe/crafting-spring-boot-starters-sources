package com.example.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.example.queuepoller.InMemoryBroker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderSeeder {

    private final InMemoryBroker broker;
    private final AtomicInteger sequence = new AtomicInteger();

    public OrderSeeder(InMemoryBroker broker) {
        this.broker = broker;
    }

    @Scheduled(fixedDelay = 2000)
    public void seed() {
        broker.send("orders", "ORDER-" + sequence.incrementAndGet());
    }
}

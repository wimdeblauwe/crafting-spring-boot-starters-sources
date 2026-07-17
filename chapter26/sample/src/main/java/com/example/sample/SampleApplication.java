package com.example.sample;

import com.example.outbox.OutboxMessage;
import com.example.outbox.OutboxPublisher;
import com.example.outbox.OutboxStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public OutboxPublisher loggingPublisher() {
        return message -> logger.info("Publishing {} -> {}: {}", message.id(), message.destination(), message.payload());
    }

    @Bean
    public CommandLineRunner seed(OutboxStore store) {
        return args -> {
            for (int i = 0; i < 3; i++) {
                OutboxMessage stored = store.append("orders", "ORDER-" + i);
                logger.info("Seeded {}", stored.id());
            }
        };
    }
}

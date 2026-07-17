package com.example.sample;

import com.example.queuepoller.InMemoryBroker;
import com.example.queuepoller.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public MessageHandler loggingHandler() {
        return message -> logger.info("Received: {}", message);
    }

    @Bean
    public CommandLineRunner seedQueue(InMemoryBroker broker) {
        return args -> {
            broker.send("orders", "ORDER-1");
            broker.send("orders", "ORDER-2");
            broker.send("orders", "ORDER-3");
        };
    }

    @Configuration
    @ConditionalOnProperty("demo.second-handler")
    static class SecondHandlerConfig {

        @Bean
        public MessageHandler auditHandler() {
            return message -> logger.info("Audit: {}", message);
        }
    }
}

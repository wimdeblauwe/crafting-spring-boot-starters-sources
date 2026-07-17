package com.example.requestid.sample;

import com.example.requestid.RequestIdGenerator;
import com.example.requestid.RequestIdGeneratorCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    @Order(0)
    RequestIdGeneratorCustomizer environmentTagCustomizer() {
        return builder -> builder.addTransform(id -> "prod:" + id);
    }

    @Bean
    @Order(10)
    RequestIdGeneratorCustomizer regionTagCustomizer() {
        return builder -> builder.addTransform(id -> "eu-west:" + id);
    }

    @Bean
    public CommandLineRunner reportGenerated(RequestIdGenerator generator) {
        return args -> {
            for (int i = 0; i < 3; i++) {
                logger.info("generated id = {}", generator.next());
            }
        };
    }
}

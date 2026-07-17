package com.example.requestid.sample;

import java.util.Optional;

import com.example.requestid.RequestIdProvider;
import com.example.requestid.RequestIdSource;
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
    RequestIdSource upstreamPropertyRequestIdSource() {
        return () -> Optional.ofNullable(System.getProperty("upstream.request-id"));
    }

    @Bean
    public CommandLineRunner reportProvided(RequestIdProvider provider) {
        return args -> {
            for (int i = 0; i < 3; i++) {
                logger.info("provider.get() = {}", provider.get());
            }
        };
    }
}

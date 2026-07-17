package com.example.sample;

import java.util.List;

import com.example.requestid.RequestIdProperties;
import com.example.requestid.RequestIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner reportProvided(Environment environment,
                                            RequestIdProperties properties,
                                            RequestIdProvider provider) {
        return args -> {
            logger.info("active profiles      = {}", List.of(environment.getActiveProfiles()));
            logger.info("generation.strategy  = {}", properties.generation().strategy());
            logger.info("generation.length    = {}", properties.generation().length());
            logger.info("propagate-headers    = {}", properties.propagateHeaders());
            logger.info("sample id            = {}", provider.get());
        };
    }
}

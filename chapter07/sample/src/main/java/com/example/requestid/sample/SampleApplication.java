package com.example.requestid.sample;

import com.example.requestid.RequestIdGenerator;
import com.example.requestid.RequestIdProperties;
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
    public CommandLineRunner reportBoundProperties(RequestIdProperties properties,
                                                   RequestIdGenerator generator) {
        return args -> {
            logger.info("Bound header  = name={}, echo={}",
                    properties.header().name(),
                    properties.header().echo());
            logger.info("Bound mdc     = enabled={}, key={}",
                    properties.mdc().enabled(),
                    properties.mdc().key());
            logger.info("Bound gen     = strategy={}, length={}",
                    properties.generation().strategy(),
                    properties.generation().length());
            logger.info("Generated id  = {}", generator.next());
        };
    }
}

package com.example.requestid.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.requestid.HeaderOverrideResolver;

@SpringBootApplication
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    // tag::commandlinerunner[]
    @Bean
    public CommandLineRunner report(ObjectProvider<HeaderOverrideResolver> resolver) {
        return args -> {
            HeaderOverrideResolver bean = resolver.getIfAvailable();
            if (bean == null) {
                logger.info("HeaderOverrideResolver is NOT registered (condition did not match).");
            } else {
                logger.info("HeaderOverrideResolver is registered with {} entries.", bean.overrides().size());
            }
        };
    }
    // end::commandlinerunner[]
}

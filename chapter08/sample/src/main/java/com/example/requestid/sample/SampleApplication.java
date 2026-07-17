package com.example.requestid.sample;

import com.example.requestid.HeaderNameResolver;
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
                                                   HeaderNameResolver resolver) {
        return args -> {
            logger.info("header           = name={}, echo={}",
                    properties.header().name(),
                    properties.header().echo());
            logger.info("mdc              = enabled={}, key={}",
                    properties.mdc().enabled(),
                    properties.mdc().key());
            logger.info("generation       = strategy={}, length={}",
                    properties.generation().strategy(),
                    properties.generation().length());
            logger.info("cache            = ttl={}, maxSize={}",
                    properties.cache().ttl(),
                    properties.cache().maxSize());
            logger.info("propagateHeaders = {}", properties.propagateHeaders());
            logger.info("pathStrategies   = {}", properties.pathStrategies());

            logger.info("resolve(/api/admin/users) = {}", resolver.resolve("/api/admin/users"));
            logger.info("resolve(/api/auth/login)  = {}", resolver.resolve("/api/auth/login"));
            logger.info("resolve(/api/orders)      = {}", resolver.resolve("/api/orders"));
        };
    }
}

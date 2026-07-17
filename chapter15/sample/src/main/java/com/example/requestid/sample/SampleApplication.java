package com.example.requestid.sample;

import java.util.Map;
import java.util.Optional;

import com.example.requestid.CountingRequestIdProvider;
import com.example.requestid.RequestIdProvider;
import com.example.requestid.RequestIdSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public CommandLineRunner reportProvided(RequestIdProvider defaultProvider,
                                            @Qualifier("requestIdProvider.web") RequestIdProvider webProvider,
                                            @Qualifier("requestIdProvider.batch") RequestIdProvider batchProvider,
                                            Map<String, CountingRequestIdProvider> counters) {
        return args -> {
            for (int i = 0; i < 3; i++) {
                logger.info("default = {}", defaultProvider.get());
                logger.info("web     = {}", webProvider.get());
                logger.info("batch   = {}", batchProvider.get());
            }
            counters.forEach((name, counter) ->
                    logger.info("counter {} -> {} calls", name, counter.count()));
        };
    }
}

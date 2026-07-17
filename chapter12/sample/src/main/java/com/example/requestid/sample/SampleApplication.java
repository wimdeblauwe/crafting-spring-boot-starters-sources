package com.example.requestid.sample;

import com.example.requestid.HeaderNameResolver;
import com.example.requestid.MdcRequestIdContributor;
import com.example.requestid.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
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
    public CommandLineRunner reportComposedBeans(RequestIdGenerator generator,
                                                 HeaderNameResolver resolver,
                                                 ObjectProvider<MdcRequestIdContributor> mdc) {
        return args -> {
            String id = generator.next();
            logger.info("generated id     = {}", id);
            logger.info("resolve(/api/orders) = {}", resolver.resolve("/api/orders"));

            mdc.ifAvailable(contributor -> {
                contributor.put(id);
                logger.info("MDC contributor present, key='{}'", contributor.key());
                contributor.clear();
            });
            if (mdc.getIfAvailable() == null) {
                logger.info("MDC contributor absent (request-id.mdc.enabled=false)");
            }
        };
    }
}

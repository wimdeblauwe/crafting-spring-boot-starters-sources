package com.example.sample;

import com.example.queuepoller.MessageHandler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public MessageHandler orderHandler() {
        return message -> logger.info("Handled: {}", message);
    }

    @Bean
    public ObservationPredicate muteScheduledTaskObservations() {
        return (name, context) -> !"tasks.scheduled.execution".equals(name);
    }

    @Bean
    public ObservationHandler<Observation.Context> loggingObservationHandler() {
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(Observation.Context context) {
                return true;
            }

            @Override
            public void onStart(Observation.Context context) {
                logger.info("Observation start: {} {}", context.getName(),
                        context.getLowCardinalityKeyValues());
            }

            @Override
            public void onStop(Observation.Context context) {
                logger.info("Observation stop:  {} {}", context.getName(),
                        context.getLowCardinalityKeyValues());
            }
        };
    }
}

package com.example.sample;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("demo.mute-poll-cycles")
public class MutePollCyclesConfiguration {

    @Bean
    public ObservationPredicate mutePollCycles() {
        return (name, context) -> !"queue.poller.cycle".equals(name);
    }
}

package com.example.clock.test;

import java.time.Clock;

import com.example.clock.ClockAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = ClockAutoConfiguration.class)
public class MutableClockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    public MutableClock clock() {
        return new MutableClock();
    }
}

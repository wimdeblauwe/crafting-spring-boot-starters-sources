package com.example.clock;

import java.time.Clock;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class ClockAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ClockAutoConfiguration.class));

    @Test
    void registersSystemClockByDefault() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(Clock.class));
    }

    @Test
    void userClockReplacesAutoConfiguredOne() {
        contextRunner
                .withUserConfiguration(UserClockConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(Clock.class);
                    assertThat(context.getBean(Clock.class).getZone()).isEqualTo(ZoneOffset.UTC);
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class UserClockConfig {

        @Bean
        Clock userClock() {
            return Clock.systemUTC();
        }
    }
}

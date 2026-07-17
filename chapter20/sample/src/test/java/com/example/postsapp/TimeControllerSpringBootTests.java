package com.example.postsapp;

import java.time.Instant;

import com.example.clock.test.AutoConfigureMutableClock;
import com.example.clock.test.MutableClock;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// tag::springboot-test[]
@SpringBootTest
@AutoConfigureMutableClock // <1>
class TimeControllerSpringBootTests {

    @Autowired
    private MutableClock clock; // <2>

    @Autowired
    private TimeController controller;

    @Test
    void controllerSeesMutableClockInstant() {
        clock.setInstant(Instant.parse("2030-06-15T12:34:56Z"));
        assertThat(controller.now().get("now")).isEqualTo("2030-06-15T12:34:56Z");
    }
}
// end::springboot-test[]

package com.example.postsapp;

import java.time.Duration;

import com.example.clock.test.MutableClock;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

// tag::webmvc-test[]
@WebMvcTest(TimeController.class)
class TimeControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MutableClock clock;

    @Test
    void returnsCurrentInstantAtClockDefault() {
        clock.reset();
        assertThat(MockMvcTester.create(mockMvc).get().uri("/now"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("now")
                .isEqualTo("2025-01-01T00:00:00Z");
    }

    @Test
    void returnsCurrentInstantAfterClockAdvances() {
        clock.reset();
        clock.advance(Duration.ofHours(2));
        assertThat(MockMvcTester.create(mockMvc).get().uri("/now"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("now")
                .isEqualTo("2025-01-01T02:00:00Z");
    }
}
// end::webmvc-test[]

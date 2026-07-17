package com.example.sample.lifecycle;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import com.example.queuepoller.InMemoryBroker;
import com.example.queuepoller.MessageHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(properties = "queue-poller.queue-name=orders")
class FirstContextTest {

    @Autowired
    InMemoryBroker broker;

    @Test
    void receivesEveryMessageItPublished() {
        IntStream.rangeClosed(1, 50).forEach(i -> broker.send("orders", "first-" + i));

        await().atMost(Duration.ofSeconds(5))
                .until(() -> CapturingHandler.RECEIVED.getOrDefault("first", List.of()).size() >= 50);

        assertThat(CapturingHandler.RECEIVED.get("first"))
                .hasSize(50)
                .allMatch(message -> message.startsWith("first-"));
        assertThat(broker.depth("orders")).isZero();
    }

    @TestConfiguration
    static class Config {

        @Bean
        MessageHandler handler() {
            return new CapturingHandler("first");
        }
    }
}

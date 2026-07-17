package com.example.postsapp;

import java.time.Clock;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    private final Clock clock;

    public TimeController(Clock clock) {
        this.clock = clock;
    }

    @GetMapping("/now")
    public Map<String, String> now() {
        return Map.of("now", clock.instant().toString());
    }
}

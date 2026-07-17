package com.example.clock.test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.util.Assert;

// tag::class[]
public class MutableClock extends Clock {

    public static final Instant DEFAULT_INSTANT = Instant.parse("2025-01-01T00:00:00Z");

    private volatile Instant instant;
    private final ZoneId zone;

    public MutableClock() {
        this(DEFAULT_INSTANT, ZoneOffset.UTC);
    }

    public MutableClock(Instant instant, ZoneId zone) {
        Assert.notNull(instant, "instant must not be null");
        Assert.notNull(zone, "zone must not be null");
        this.instant = instant;
        this.zone = zone;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new MutableClock(this.instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        Assert.notNull(instant, "instant must not be null");
        this.instant = instant;
    }

    public void advance(Duration duration) {
        Assert.notNull(duration, "duration must not be null");
        this.instant = this.instant.plus(duration);
    }

    public void reset() {
        this.instant = DEFAULT_INSTANT;
    }
}
// end::class[]

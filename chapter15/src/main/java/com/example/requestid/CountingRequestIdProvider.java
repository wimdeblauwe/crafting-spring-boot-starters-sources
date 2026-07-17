package com.example.requestid;

import java.util.concurrent.atomic.AtomicLong;

public class CountingRequestIdProvider implements RequestIdProvider {

    private final String name;
    private final RequestIdProvider delegate;
    private final AtomicLong count = new AtomicLong();

    public CountingRequestIdProvider(String name, RequestIdProvider delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public String get() {
        count.incrementAndGet();
        return delegate.get();
    }

    public String name() {
        return name;
    }

    public long count() {
        return count.get();
    }
}

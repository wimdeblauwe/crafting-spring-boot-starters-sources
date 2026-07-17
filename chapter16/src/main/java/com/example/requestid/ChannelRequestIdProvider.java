package com.example.requestid;

public class ChannelRequestIdProvider implements RequestIdProvider {

    private final String prefix;
    private final RequestIdGenerator generator;

    public ChannelRequestIdProvider(String prefix, RequestIdGenerator generator) {
        this.prefix = prefix;
        this.generator = generator;
    }

    @Override
    public String get() {
        return prefix + generator.next();
    }
}

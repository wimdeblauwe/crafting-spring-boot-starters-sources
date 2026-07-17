package com.example.requestid;

import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;

public class RequestIdProvider {

    private final ObjectProvider<RequestIdSource> sources;
    private final RequestIdGenerator generator;

    public RequestIdProvider(ObjectProvider<RequestIdSource> sources,
                             RequestIdGenerator generator) {
        this.sources = sources;
        this.generator = generator;
    }

    public String get() {
        return sources.orderedStream()
                .map(RequestIdSource::read)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseGet(generator::next);
    }
}

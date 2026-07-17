package com.example.requestid;

import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;

public class DefaultRequestIdProvider implements RequestIdProvider {

    private final ObjectProvider<RequestIdSource> sources;
    private final RequestIdGenerator generator;

    public DefaultRequestIdProvider(ObjectProvider<RequestIdSource> sources,
                                    RequestIdGenerator generator) {
        this.sources = sources;
        this.generator = generator;
    }

    @Override
    public String get() {
        return sources.orderedStream()
                .map(RequestIdSource::read)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseGet(generator::next);
    }
}

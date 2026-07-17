package com.example.requestid;

@FunctionalInterface
public interface RequestIdGeneratorCustomizer {

    void customize(RequestIdGeneratorBuilder builder);
}

package com.example.requestid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class RequestIdGeneratorBuilder {

    private RequestIdProperties.Generation generation;
    private final List<UnaryOperator<String>> transforms = new ArrayList<>();

    public RequestIdGeneratorBuilder generation(RequestIdProperties.Generation generation) {
        this.generation = generation;
        return this;
    }

    public RequestIdGeneratorBuilder addTransform(UnaryOperator<String> transform) {
        this.transforms.add(transform);
        return this;
    }

    public RequestIdGenerator build() {
        return new RequestIdGenerator(generation, transforms);
    }
}

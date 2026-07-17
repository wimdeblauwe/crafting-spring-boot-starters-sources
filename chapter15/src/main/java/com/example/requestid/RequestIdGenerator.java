package com.example.requestid;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class RequestIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final RequestIdProperties.Generation generation;
    private final List<UnaryOperator<String>> transforms;

    public RequestIdGenerator(RequestIdProperties.Generation generation,
                              List<UnaryOperator<String>> transforms) {
        this.generation = generation;
        this.transforms = List.copyOf(transforms);
    }

    public String next() {
        String id = switch (generation.strategy()) {
            case UUID -> UUID.randomUUID().toString();
            case RANDOM_HEX -> randomHex(generation.length());
        };
        for (UnaryOperator<String> transform : transforms) {
            id = transform.apply(id);
        }
        return id;
    }

    private static String randomHex(int length) {
        byte[] bytes = new byte[(length + 1) / 2];
        RANDOM.nextBytes(bytes);
        String hex = HexFormat.of().formatHex(bytes);
        return hex.substring(0, length);
    }
}

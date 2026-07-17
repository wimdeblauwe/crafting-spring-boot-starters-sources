package com.example.requestid;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;

public class RequestIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final RequestIdProperties.Generation generation;

    public RequestIdGenerator(RequestIdProperties.Generation generation) {
        this.generation = generation;
    }

    public String next() {
        return switch (generation.strategy()) {
            case UUID -> UUID.randomUUID().toString();
            case RANDOM_HEX -> randomHex(generation.length());
        };
    }

    private static String randomHex(int length) {
        byte[] bytes = new byte[(length + 1) / 2];
        RANDOM.nextBytes(bytes);
        String hex = HexFormat.of().formatHex(bytes);
        return hex.substring(0, length);
    }
}

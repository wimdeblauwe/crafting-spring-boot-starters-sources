package com.example.requestid;

import java.util.Optional;

@FunctionalInterface
public interface RequestIdSource {

    Optional<String> read();
}

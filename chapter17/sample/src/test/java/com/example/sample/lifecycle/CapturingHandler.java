package com.example.sample.lifecycle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.queuepoller.MessageHandler;

public class CapturingHandler implements MessageHandler {

    public static final Map<String, List<String>> RECEIVED = new ConcurrentHashMap<>();

    private final String tag;

    public CapturingHandler(String tag) {
        this.tag = tag;
        RECEIVED.computeIfAbsent(tag, key -> new CopyOnWriteArrayList<>());
    }

    @Override
    public void handle(String message) {
        RECEIVED.get(tag).add(message);
    }

    public List<String> received() {
        return RECEIVED.get(tag);
    }
}

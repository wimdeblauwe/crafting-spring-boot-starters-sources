package com.example.queuepoller;

import java.util.List;

public class MultipleMessageHandlersException extends QueuePollerStartupException {

    private final List<String> handlerBeanNames;

    public MultipleMessageHandlersException(List<String> handlerBeanNames) {
        super("Found %d MessageHandler beans, expected exactly one: %s"
                .formatted(handlerBeanNames.size(), handlerBeanNames));
        this.handlerBeanNames = List.copyOf(handlerBeanNames);
    }

    public List<String> handlerBeanNames() {
        return handlerBeanNames;
    }
}

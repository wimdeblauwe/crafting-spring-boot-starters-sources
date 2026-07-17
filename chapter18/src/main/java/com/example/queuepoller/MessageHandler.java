package com.example.queuepoller;

@FunctionalInterface
public interface MessageHandler {

    void handle(String message);
}

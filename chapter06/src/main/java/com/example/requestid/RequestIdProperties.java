package com.example.requestid;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("request-id")
public record RequestIdProperties(Map<String, String> headerOverrides) {
}

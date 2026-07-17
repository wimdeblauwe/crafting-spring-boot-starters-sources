package com.example.requestid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("request-id")
public record RequestIdProperties(
        @DefaultValue("X-Request-Id") String headerName,
        @DefaultValue("requestId") String mdcKey) {
}

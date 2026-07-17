package com.example.requestid;

import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class HeaderNameResolver {

    private final PathMatcher matcher = new AntPathMatcher();

    private final Map<String, String> pathStrategies;
    private final String defaultHeaderName;

    public HeaderNameResolver(Map<String, String> pathStrategies, String defaultHeaderName) {
        this.pathStrategies = pathStrategies;
        this.defaultHeaderName = defaultHeaderName;
    }

    public String resolve(String requestPath) {
        for (Map.Entry<String, String> entry : pathStrategies.entrySet()) {
            if (matcher.match(entry.getKey(), requestPath)) {
                return entry.getValue();
            }
        }
        return defaultHeaderName;
    }
}

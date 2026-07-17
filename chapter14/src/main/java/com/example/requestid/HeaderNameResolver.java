package com.example.requestid;

import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class HeaderNameResolver {

    private final PathMatcher matcher = new AntPathMatcher();

    private final Map<String, HeaderName> pathStrategies;
    private final HeaderName defaultHeaderName;

    public HeaderNameResolver(Map<String, HeaderName> pathStrategies, HeaderName defaultHeaderName) {
        this.pathStrategies = pathStrategies;
        this.defaultHeaderName = defaultHeaderName;
    }

    public HeaderName resolve(String requestPath) {
        for (Map.Entry<String, HeaderName> entry : pathStrategies.entrySet()) {
            if (matcher.match(entry.getKey(), requestPath)) {
                return entry.getValue();
            }
        }
        return defaultHeaderName;
    }
}

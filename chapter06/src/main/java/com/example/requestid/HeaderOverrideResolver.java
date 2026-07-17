package com.example.requestid;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeaderOverrideResolver {

    private static final Logger logger = LoggerFactory.getLogger(HeaderOverrideResolver.class);

    private final Map<String, String> overrides;

    public HeaderOverrideResolver(Map<String, String> overrides) {
        this.overrides = overrides;
        logger.info("HeaderOverrideResolver registered with {} override(s).", overrides.size());
    }

    public Map<String, String> overrides() {
        return overrides;
    }
}

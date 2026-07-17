package com.example.requestid;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * Value object for an HTTP header name.
 * <p>
 * Validates its input on construction and throws {@link IllegalArgumentException}
 * for anything that cannot be a valid header name.
 */
public record HeaderName(String value) {

    private static final Pattern VALID = Pattern.compile("[A-Za-z][A-Za-z0-9-]*");

    public HeaderName {
        Assert.hasText(value, "The HeaderName value should have text");
        Assert.isTrue(VALID.matcher(value).matches(),
                () -> "Not a valid HTTP header name: '" + value + "'");
    }

    @Override
    public String toString() {
        return value;
    }
}

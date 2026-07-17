package com.example.outbox;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonValue;

import org.springframework.util.Assert;

public record SecureString(char[] value) {

    public SecureString {
        Assert.notNull(value, "value must not be null");
        value = Arrays.copyOf(value, value.length);
    }

    public static SecureString of(String value) {
        return new SecureString(value.toCharArray());
    }

    @Override
    public char[] value() {
        return Arrays.copyOf(value, value.length);
    }

    public String valueAsString() {
        return new String(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecureString that = (SecureString) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return "<REDACTED>";
    }
}

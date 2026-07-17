package com.example.slug;

import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

public class SlugGenerator {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]+");

    private final Normaliser normaliser;
    private final SlugProperties properties;

    public SlugGenerator(Normaliser normaliser, SlugProperties properties) {
        this.normaliser = normaliser;
        this.properties = properties;
    }

    public String slugify(String input) {
        Assert.notNull(input, "input must not be null");
        String s = normaliser.normalise(input).trim();
        if (properties.lowercase()) {
            s = s.toLowerCase(Locale.ROOT);
        }
        s = NON_ALPHANUMERIC.matcher(s).replaceAll(properties.separator());
        s = trimSeparator(s, properties.separator());
        if (s.length() > properties.maxLength()) {
            s = trimSeparator(s.substring(0, properties.maxLength()), properties.separator());
        }
        return s;
    }

    private static String trimSeparator(String value, String separator) {
        while (value.startsWith(separator)) {
            value = value.substring(separator.length());
        }
        while (value.endsWith(separator)) {
            value = value.substring(0, value.length() - separator.length());
        }
        return value;
    }
}

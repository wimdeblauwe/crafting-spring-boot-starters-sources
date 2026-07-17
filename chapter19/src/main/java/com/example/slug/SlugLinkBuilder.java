package com.example.slug;

import org.springframework.util.Assert;

public class SlugLinkBuilder {

    private final SlugGenerator slugGenerator;

    public SlugLinkBuilder(SlugGenerator slugGenerator) {
        this.slugGenerator = slugGenerator;
    }

    public String buildPath(String prefix, long id, String title) {
        Assert.hasText(prefix, "prefix must not be blank");
        String normalisedPrefix = prefix.startsWith("/") ? prefix : "/" + prefix;
        return normalisedPrefix + "/" + id + "-" + slugGenerator.slugify(title);
    }
}

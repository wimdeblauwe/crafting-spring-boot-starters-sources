package com.example.slug;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

@Validated
@ConfigurationProperties(prefix = "slug")
public record SlugProperties(
        @DefaultValue("true") boolean enabled,
        @DefaultValue("-") String separator,
        @DefaultValue("80") int maxLength,
        @DefaultValue("true") boolean lowercase
) {

    public SlugProperties {
        Assert.hasText(separator, "slug.separator must not be blank");
        Assert.isTrue(maxLength > 0, "slug.max-length must be positive");
    }
}

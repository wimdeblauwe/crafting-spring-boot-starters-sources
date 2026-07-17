package com.example.requestid;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("request-id")
public record RequestIdProperties(
        @DefaultValue @Valid Header header,
        @DefaultValue @Valid Mdc mdc,
        @DefaultValue @Valid Generation generation,
        @DefaultValue @Valid Cache cache,
        @DefaultValue List<String> propagateHeaders,
        @DefaultValue Map<String, String> pathStrategies) {

    public record Header(
            @DefaultValue("X-Request-Id")
            @NotBlank
            @Pattern(regexp = "[A-Za-z][A-Za-z0-9-]*",
                    message = "must be a valid HTTP header name")
            String name,
            @DefaultValue("true") boolean echo) {
    }

    public record Mdc(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("requestId") @NotBlank String key) {
    }

    public record Generation(
            @DefaultValue("UUID") Strategy strategy,
            @DefaultValue("16") @Min(8) @Max(64) int length) {

        public enum Strategy { UUID, RANDOM_HEX }
    }

    public record Cache(
            @DefaultValue("5m") Duration ttl,
            @DefaultValue("1MB") DataSize maxSize) {
    }
}

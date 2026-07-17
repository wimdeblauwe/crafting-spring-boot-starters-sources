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

/**
 * Configuration for the request-id starter.
 * <p>
 * The starter assigns a stable identifier to every incoming HTTP request and
 * makes it available to logs and downstream calls. The properties below control
 * which header carries the value, how it is generated, and how it is propagated.
 *
 * @param header           How the request id is read from and written to HTTP headers.
 * @param mdc              How the request id is exposed to the logging MDC.
 * @param generation       How a new request id is generated when none is present.
 * @param cache            In-memory cache used to look up request ids by correlation key.
 * @param propagateHeaders Header names that, if present on the inbound request, are copied
 *                         onto outbound calls made within the same request scope.
 * @param pathStrategies   Per-path overrides of the inbound header name. Keys are Ant-style
 *                         path patterns; values are the header name to read for matching requests.
 */
@Validated
@ConfigurationProperties("request-id")
public record RequestIdProperties(
        @DefaultValue @Valid Header header,
        @DefaultValue @Valid Mdc mdc,
        @DefaultValue @Valid Generation generation,
        @DefaultValue @Valid Cache cache,
        @DefaultValue List<String> propagateHeaders,
        @DefaultValue Map<String, String> pathStrategies) {

    // tag::header-javadoc[]
    /**
     * Header settings.
     *
     * @param name The inbound header name to read the
     *             request id from. Must be a valid
     *             HTTP header name (letters, digits,
     *             hyphens; starting with a letter).
     * @param echo When true, the same header is written
     *             on the response so the client can see
     *             which id the server assigned.
     */
    public record Header(
            @DefaultValue("X-Request-Id")
            @NotBlank
            @Pattern(regexp = "[A-Za-z][A-Za-z0-9-]*",
                    message = "must be a valid HTTP header name")
            String name,
            @DefaultValue("true") boolean echo) {
    }
    // end::header-javadoc[]

    /**
     * MDC integration.
     *
     * @param enabled When true, the request id is placed in the SLF4J MDC for the
     *                duration of the request, so that log statements include it automatically.
     * @param key     The MDC key under which the request id is stored.
     */
    public record Mdc(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("requestId") @NotBlank String key) {
    }

    /**
     * Request id generation settings.
     *
     * @param strategy How a new request id is produced when the inbound request did not carry one.
     * @param length   Length of the generated id, in characters. Only applies to the RANDOM_HEX
     *                 strategy; UUIDs always have their natural length.
     */
    public record Generation(
            @DefaultValue("UUID") Strategy strategy,
            @DefaultValue("16") @Min(8) @Max(64) int length) {

        public enum Strategy { UUID, RANDOM_HEX }
    }

    /**
     * In-memory cache settings for correlated lookups.
     *
     * @param ttl     Time-to-live for an individual entry.
     * @param maxSize Approximate maximum size of the cache. The cache evicts oldest entries
     *                when this size is exceeded.
     */
    public record Cache(
            @DefaultValue("5m") Duration ttl,
            @DefaultValue("1MB") DataSize maxSize) {
    }
}

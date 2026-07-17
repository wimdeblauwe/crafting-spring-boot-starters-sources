package com.example.requestid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration for the request-id starter.
 *
 * @param headerName HTTP header that carries the request identifier on incoming and outgoing calls.
 * @param mdcKey Key the request identifier is stored under in the SLF4J MDC.
 * @param propagateToRestClient Whether outbound RestClient calls should forward the current request identifier.
 */
@ConfigurationProperties("request-id")
public record RequestIdProperties(
        @DefaultValue("X-Request-Id") String headerName,
        @DefaultValue("requestId") String mdcKey,
        @DefaultValue("true") boolean propagateToRestClient) {
}

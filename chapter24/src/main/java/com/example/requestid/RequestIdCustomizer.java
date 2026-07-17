package com.example.requestid;

// tag::interface[]
/**
 * Implement and publish as a bean to observe or replace the request identifier
 * before it is written to the response header and the MDC.
 */
@FunctionalInterface
public interface RequestIdCustomizer {

    /**
     * Returns the identifier to use for the current request.
     *
     * @param incoming the identifier the starter resolved from the request, or {@code null} if none was present
     * @return the identifier to write to the response header and MDC
     */
    String customize(String incoming);
}
// end::interface[]

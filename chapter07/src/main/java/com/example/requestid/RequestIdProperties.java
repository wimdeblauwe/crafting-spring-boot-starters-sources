package com.example.requestid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("request-id")
public record RequestIdProperties(
        @DefaultValue Header header,
        @DefaultValue Mdc mdc,
        @DefaultValue Generation generation) {

    public record Header(
            @DefaultValue("X-Request-Id") String name,
            @DefaultValue("true") boolean echo) {
    }

    public record Mdc(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("requestId") String key) {
    }

    public record Generation(
            @DefaultValue("UUID") Strategy strategy,
            @DefaultValue("16") int length) {

        public enum Strategy { UUID, RANDOM_HEX }
    }
}

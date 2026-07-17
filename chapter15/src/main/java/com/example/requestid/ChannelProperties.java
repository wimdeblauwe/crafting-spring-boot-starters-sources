package com.example.requestid;

import org.springframework.util.Assert;

public record ChannelProperties(String prefix) {

    public ChannelProperties {
        Assert.hasText(prefix, "request-id.channels.*.prefix must be set to a non-blank value");
    }
}

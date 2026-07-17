package com.example.requestid;

import org.slf4j.MDC;

public class MdcRequestIdContributor {

    private final RequestIdProperties.Mdc mdc;

    public MdcRequestIdContributor(RequestIdProperties.Mdc mdc) {
        this.mdc = mdc;
    }

    public void put(String requestId) {
        MDC.put(mdc.key(), requestId);
    }

    public void clear() {
        MDC.remove(mdc.key());
    }

    public String key() {
        return mdc.key();
    }
}

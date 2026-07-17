package com.example.requestid;

import org.springframework.core.convert.converter.Converter;

public class StringToHeaderNameConverter implements Converter<String, HeaderName> {

    @Override
    public HeaderName convert(String source) {
        return new HeaderName(source);
    }
}

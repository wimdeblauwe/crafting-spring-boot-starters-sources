package com.example.requestid;

import java.util.Map;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

public class RequestIdChannelRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {
        Map<String, ChannelProperties> channels = Binder.get(env)
                .bind("request-id.channels", Bindable.mapOf(String.class, ChannelProperties.class))
                .orElse(Map.of());

        channels.forEach((name, properties) ->
                registry.registerBean("requestIdProvider." + name, ChannelRequestIdProvider.class, spec -> spec
                        .supplier(context -> new ChannelRequestIdProvider(
                                properties.prefix(),
                                context.bean(RequestIdGenerator.class)))));
    }
}

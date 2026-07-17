package com.example.requestid;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class RequestIdChannelRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String, ChannelProperties> channels = Binder.get(environment)
                .bind("request-id.channels", Bindable.mapOf(String.class, ChannelProperties.class))
                .orElse(Map.of());

        channels.forEach((name, properties) -> register(registry, name, properties));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    private void register(BeanDefinitionRegistry registry, String channelName, ChannelProperties properties) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(ChannelRequestIdProvider.class)
                .addConstructorArgValue(properties.prefix())
                .addConstructorArgReference("requestIdGenerator");
        registry.registerBeanDefinition("requestIdProvider." + channelName, builder.getBeanDefinition());
    }
}

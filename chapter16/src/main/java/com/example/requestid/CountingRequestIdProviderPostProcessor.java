package com.example.requestid;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CountingRequestIdProviderPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CountingRequestIdProvider) {
            return bean;
        }
        if (bean instanceof RequestIdProvider provider) {
            return new CountingRequestIdProvider(beanName, provider);
        }
        return bean;
    }
}

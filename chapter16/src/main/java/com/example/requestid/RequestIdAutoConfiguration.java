package com.example.requestid;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;

@AutoConfiguration
@EnableConfigurationProperties(RequestIdProperties.class)
public class RequestIdAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class CoreConfiguration {

        @Bean
        @ConfigurationPropertiesBinding
        Converter<String, HeaderName> stringToHeaderNameConverter() {
            return new StringToHeaderNameConverter();
        }

        @Bean
        @ConditionalOnMissingBean
        RequestIdGenerator requestIdGenerator(RequestIdProperties properties,
                                              ObjectProvider<RequestIdGeneratorCustomizer> customizers) {
            RequestIdGeneratorBuilder builder = new RequestIdGeneratorBuilder()
                    .generation(properties.generation());
            customizers.orderedStream().forEach(customizer -> customizer.customize(builder));
            return builder.build();
        }

        @Bean
        @Primary
        @ConditionalOnMissingBean(name = "requestIdProvider")
        DefaultRequestIdProvider requestIdProvider(ObjectProvider<RequestIdSource> sources,
                                                   RequestIdGenerator generator) {
            return new DefaultRequestIdProvider(sources, generator);
        }

        @Bean
        HeaderNameResolver headerNameResolver(RequestIdProperties properties) {
            return new HeaderNameResolver(properties.pathStrategies(), properties.header().name());
        }

        @Bean
        static RequestIdChannelRegistrar requestIdChannelRegistrar() {
            return new RequestIdChannelRegistrar();
        }

        @Bean
        static CountingRequestIdProviderPostProcessor countingRequestIdProviderPostProcessor() {
            return new CountingRequestIdProviderPostProcessor();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "request-id.mdc.enabled", matchIfMissing = true)
    static class MdcConfiguration {

        @Bean
        MdcRequestIdContributor mdcRequestIdContributor(RequestIdProperties properties) {
            return new MdcRequestIdContributor(properties.mdc());
        }
    }
}

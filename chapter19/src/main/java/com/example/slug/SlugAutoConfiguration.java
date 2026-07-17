package com.example.slug;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(SlugProperties.class)
@ConditionalOnProperty(name = "slug.enabled", matchIfMissing = true)
public class SlugAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Normaliser.class)
    @ConditionalOnClass(name = "com.ibm.icu.text.Transliterator")
    public Normaliser icuNormaliser() {
        return new IcuNormaliser();
    }

    @Bean
    @ConditionalOnMissingBean(Normaliser.class)
    public Normaliser javaTextNormaliser() {
        return new JavaTextNormaliser();
    }

    @Bean
    @ConditionalOnMissingBean
    public SlugGenerator slugGenerator(Normaliser normaliser, SlugProperties properties) {
        return new SlugGenerator(normaliser, properties);
    }
}

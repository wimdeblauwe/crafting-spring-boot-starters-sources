package com.example.slug;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = SlugAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(SlugGenerator.class)
public class SlugWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SlugLinkBuilder slugLinkBuilder(SlugGenerator slugGenerator) {
        return new SlugLinkBuilder(slugGenerator);
    }
}

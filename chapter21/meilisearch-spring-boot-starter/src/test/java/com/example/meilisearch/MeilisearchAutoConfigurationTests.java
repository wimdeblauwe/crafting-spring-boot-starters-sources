package com.example.meilisearch;

import com.meilisearch.sdk.Client;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class MeilisearchAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MeilisearchAutoConfiguration.class));

    @Test
    void registersClientAndDefaultConnectionDetailsWhenNoneProvided() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(Client.class);
            assertThat(context).hasSingleBean(MeilisearchConnectionDetails.class);
            MeilisearchConnectionDetails details = context.getBean(MeilisearchConnectionDetails.class);
            assertThat(details.getHostUrl()).isEqualTo("http://localhost:7700");
        });
    }

    @Test
    void honoursPropertyOverrides() {
        contextRunner
                .withPropertyValues("meilisearch.host=search.internal", "meilisearch.port=9200", "meilisearch.api-key=secret")
                .run(context -> {
                    MeilisearchConnectionDetails details = context.getBean(MeilisearchConnectionDetails.class);
                    assertThat(details.getHostUrl()).isEqualTo("http://search.internal:9200");
                    assertThat(details.getApiKey()).isEqualTo("secret");
                });
    }

    @Test
    void userConnectionDetailsBeanReplacesDefault() {
        contextRunner
                .withUserConfiguration(CustomConnectionDetailsConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(MeilisearchConnectionDetails.class);
                    assertThat(context.getBean(MeilisearchConnectionDetails.class))
                            .isNotInstanceOf(PropertiesMeilisearchConnectionDetails.class);
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomConnectionDetailsConfig {
        @Bean
        MeilisearchConnectionDetails customDetails() {
            return new MeilisearchConnectionDetails() {
                @Override
                public String getHost() {
                    return "elsewhere";
                }

                @Override
                public int getPort() {
                    return 1234;
                }

                @Override
                public String getApiKey() {
                    return "k";
                }
            };
        }
    }
}

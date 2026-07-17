package com.example.slug;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class SlugWebAutoConfigurationTests {

    private final WebApplicationContextRunner webContextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SlugAutoConfiguration.class,
                    SlugWebAutoConfiguration.class));

    private final ApplicationContextRunner nonWebContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SlugAutoConfiguration.class,
                    SlugWebAutoConfiguration.class));

    private final ReactiveWebApplicationContextRunner reactiveContextRunner =
            new ReactiveWebApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(
                            SlugAutoConfiguration.class,
                            SlugWebAutoConfiguration.class));

    // tag::web-positive[]
    @Test
    void registersSlugLinkBuilderInServletWebContext() {
        webContextRunner.run(context -> {
            assertThat(context).hasSingleBean(SlugLinkBuilder.class);
            assertThat(context.getBean(SlugLinkBuilder.class).buildPath("posts", 42, "Hello World"))
                    .isEqualTo("/posts/42-hello-world");
        });
    }
    // end::web-positive[]

    // tag::web-negative[]
    @Test
    void slugLinkBuilderIsAbsentInNonWebContext() {
        nonWebContextRunner.run(context -> {
            assertThat(context).hasSingleBean(SlugGenerator.class);
            assertThat(context).doesNotHaveBean(SlugLinkBuilder.class);
        });
    }

    @Test
    void slugLinkBuilderIsAbsentInReactiveWebContext() {
        reactiveContextRunner.run(context -> {
            assertThat(context).hasSingleBean(SlugGenerator.class);
            assertThat(context).doesNotHaveBean(SlugLinkBuilder.class);
        });
    }
    // end::web-negative[]

    @Test
    void slugLinkBuilderIsAbsentWhenSlugGeneratorIsAbsent() {
        webContextRunner
                .withPropertyValues("slug.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(SlugGenerator.class);
                    assertThat(context).doesNotHaveBean(SlugLinkBuilder.class);
                });
    }

    @Test
    void userSlugLinkBuilderReplacesAutoConfiguredOne() {
        webContextRunner
                .withUserConfiguration(UserLinkBuilderConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(SlugLinkBuilder.class);
                    assertThat(context.getBean(SlugLinkBuilder.class))
                            .isSameAs(context.getBean("userSlugLinkBuilder"));
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class UserLinkBuilderConfig {

        @Bean
        SlugLinkBuilder userSlugLinkBuilder(SlugGenerator slugGenerator) {
            return new SlugLinkBuilder(slugGenerator);
        }
    }
}

package com.example.slug;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class SlugAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SlugAutoConfiguration.class));

    // tag::registers-defaults[]
    @Test
    void registersSlugGeneratorByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SlugGenerator.class);
            assertThat(context).hasSingleBean(Normaliser.class);
            assertThat(context).hasSingleBean(SlugProperties.class);
        });
    }
    // end::registers-defaults[]

    // tag::disabled-property[]
    @Test
    void slugGeneratorIsAbsentWhenStarterIsDisabled() {
        contextRunner
                .withPropertyValues("slug.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(SlugGenerator.class);
                    assertThat(context).doesNotHaveBean(Normaliser.class);
                });
    }
    // end::disabled-property[]

    // tag::separator-property[]
    @Test
    void slugifyUsesConfiguredSeparator() {
        contextRunner
                .withPropertyValues("slug.separator=_")
                .run(context -> {
                    SlugGenerator generator = context.getBean(SlugGenerator.class);
                    assertThat(generator.slugify("a b")).isEqualTo("a_b");
                });
    }
    // end::separator-property[]

    @Test
    void slugifyPreservesCaseWhenLowercaseIsFalse() {
        contextRunner
                .withPropertyValues("slug.lowercase=false")
                .run(context -> {
                    SlugGenerator generator = context.getBean(SlugGenerator.class);
                    assertThat(generator.slugify("Hello World")).isEqualTo("Hello-World");
                });
    }

    @Test
    void slugifyTruncatesAtMaxLength() {
        contextRunner
                .withPropertyValues("slug.max-length=5")
                .run(context -> {
                    SlugGenerator generator = context.getBean(SlugGenerator.class);
                    assertThat(generator.slugify("Hello World")).isEqualTo("hello");
                });
    }

    // tag::invalid-max-length[]
    @Test
    void contextFailsWhenMaxLengthIsZero() {
        contextRunner
                .withPropertyValues("slug.max-length=0")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context).getFailure()
                            .rootCause()
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("slug.max-length must be positive");
                });
    }
    // end::invalid-max-length[]

    @Test
    void contextFailsWhenSeparatorIsBlank() {
        contextRunner
                .withPropertyValues("slug.separator=")
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context).getFailure()
                            .rootCause()
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("slug.separator must not be blank");
                });
    }

    // tag::user-override[]
    @Test
    void userSlugGeneratorReplacesAutoConfiguredOne() {
        contextRunner
                .withUserConfiguration(UserSlugConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(SlugGenerator.class);
                    assertThat(context.getBean(SlugGenerator.class))
                            .isSameAs(context.getBean("userSlugGenerator"));
                });
    }
    // end::user-override[]

    // tag::icu-present[]
    @Test
    void icuNormaliserIsUsedWhenIcuIsOnClasspath() {
        contextRunner.run(context -> {
            assertThat(context.getBean(Normaliser.class))
                    .isInstanceOf(IcuNormaliser.class);
            assertThat(context.getBean(SlugGenerator.class).slugify("Café Olé"))
                    .isEqualTo("cafe-ole");
        });
    }
    // end::icu-present[]

    // tag::icu-filtered[]
    @Test
    void javaTextNormaliserIsUsedWhenIcuIsFiltered() {
        contextRunner
                .withClassLoader(new FilteredClassLoader("com.ibm.icu.text.Transliterator"))
                .run(context -> {
                    assertThat(context.getBean(Normaliser.class))
                            .isInstanceOf(JavaTextNormaliser.class);
                    assertThat(context.getBean(SlugGenerator.class).slugify("Café Olé"))
                            .isEqualTo("cafe-ole");
                });
    }
    // end::icu-filtered[]

    // tag::user-bean[]
    @Test
    void userNormaliserReplacesBothAutoConfiguredVariants() {
        contextRunner
                .withBean("userNormaliser", Normaliser.class, () -> input -> "FIXED")
                .run(context -> {
                    assertThat(context).hasSingleBean(Normaliser.class);
                    assertThat(context.getBean(SlugGenerator.class).slugify("anything"))
                            .isEqualTo("fixed");
                });
    }
    // end::user-bean[]

    // tag::user-config-class[]
    @Configuration(proxyBeanMethods = false)
    static class UserSlugConfig {

        @Bean
        SlugGenerator userSlugGenerator() {
            SlugProperties properties = new SlugProperties(true, "-", 80, true);
            return new SlugGenerator(new JavaTextNormaliser(), properties);
        }
    }
    // end::user-config-class[]
}

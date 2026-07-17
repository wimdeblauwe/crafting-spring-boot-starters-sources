package com.example.requestid;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RequestIdEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String BASE_RESOURCE = "request-id-defaults.properties";
    private static final String PROFILE_RESOURCE_FORMAT = "request-id-defaults-%s.properties";

    private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources sources = environment.getPropertySources();

        PropertySource<?> baseSource = load(BASE_RESOURCE);
        if (baseSource != null) {
            sources.addLast(baseSource);
        }

        String anchor = baseSource != null ? baseSource.getName() : null;
        for (String profile : environment.getActiveProfiles()) {
            PropertySource<?> profileSource = load(PROFILE_RESOURCE_FORMAT.formatted(profile));
            if (profileSource == null) {
                continue;
            }
            if (anchor != null) {
                sources.addBefore(anchor, profileSource);
            } else {
                sources.addLast(profileSource);
            }
            anchor = profileSource.getName();
        }
    }

    @Override
    public int getOrder() {
        return ConfigDataEnvironmentPostProcessor.ORDER + 1;
    }

    private PropertySource<?> load(String resourceLocation) {
        Resource resource = new ClassPathResource(resourceLocation);
        if (!resource.exists()) {
            return null;
        }
        try {
            List<PropertySource<?>> loaded = loader.load("request-id [" + resourceLocation + "]", resource);
            return loaded.isEmpty() ? null : loaded.get(0);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load " + resourceLocation, ex);
        }
    }
}

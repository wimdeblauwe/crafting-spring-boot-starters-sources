package com.example.meilisearch;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

public interface MeilisearchConnectionDetails extends ConnectionDetails {

    String getHost();

    int getPort();

    String getApiKey();

    default String getHostUrl() {
        return "http://" + getHost() + ":" + getPort();
    }
}

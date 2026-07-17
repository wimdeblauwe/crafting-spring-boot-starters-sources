package com.example.meilisearch.test;

import com.example.meilisearch.MeilisearchConnectionDetails;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

class MeilisearchContainerConnectionDetailsFactory
        extends ContainerConnectionDetailsFactory<MeilisearchContainer, MeilisearchConnectionDetails> {

    @Override
    protected MeilisearchConnectionDetails getContainerConnectionDetails(
            ContainerConnectionSource<MeilisearchContainer> source) {
        return new MeilisearchContainerConnectionDetails(source);
    }

    private static final class MeilisearchContainerConnectionDetails
            extends ContainerConnectionDetailsFactory.ContainerConnectionDetails<MeilisearchContainer>
            implements MeilisearchConnectionDetails {

        private MeilisearchContainerConnectionDetails(ContainerConnectionSource<MeilisearchContainer> source) {
            super(source);
        }

        @Override
        public String getHost() {
            return getContainer().getHost();
        }

        @Override
        public int getPort() {
            return getContainer().getMeilisearchPort();
        }

        @Override
        public String getApiKey() {
            return getContainer().getMasterKey();
        }
    }
}

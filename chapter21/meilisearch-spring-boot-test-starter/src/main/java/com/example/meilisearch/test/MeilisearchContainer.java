package com.example.meilisearch.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class MeilisearchContainer extends GenericContainer<MeilisearchContainer> {

    private static final DockerImageName DEFAULT_IMAGE = DockerImageName.parse("getmeili/meilisearch");

    private static final int MEILISEARCH_PORT = 7700;

    private static final String DEFAULT_MASTER_KEY = "masterKey";

    private String masterKey = DEFAULT_MASTER_KEY;

    public MeilisearchContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public MeilisearchContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE);
        withExposedPorts(MEILISEARCH_PORT);
        withEnv("MEILI_MASTER_KEY", DEFAULT_MASTER_KEY);
        withEnv("MEILI_NO_ANALYTICS", "true");
        waitingFor(Wait.forHttp("/health").forPort(MEILISEARCH_PORT));
    }

    public MeilisearchContainer withMasterKey(String masterKey) {
        this.masterKey = masterKey;
        withEnv("MEILI_MASTER_KEY", masterKey);
        return self();
    }

    public String getMasterKey() {
        return masterKey;
    }

    public int getMeilisearchPort() {
        return getMappedPort(MEILISEARCH_PORT);
    }
}

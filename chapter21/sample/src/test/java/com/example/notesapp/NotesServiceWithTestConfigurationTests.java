package com.example.notesapp;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

// tag::testconfig-test[]
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class NotesServiceWithTestConfigurationTests {

    @Autowired
    NotesService notes;

    @Test
    void indexesAndSearchesNotes() throws Exception {
        notes.index(
                new Note("1", "Spring Boot", "Auto-configuration that just works"),
                new Note("2", "Meilisearch", "Lightning-fast search engine"),
                new Note("3", "Testcontainers", "Real services in tests"));

        List<Note> hits = notes.search("search");

        assertThat(hits).extracting(Note::id).contains("2");
    }
}
// end::testconfig-test[]

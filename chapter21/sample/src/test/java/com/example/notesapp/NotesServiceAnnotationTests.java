package com.example.notesapp;

import java.util.List;

import com.example.meilisearch.test.AutoConfigureMeilisearchContainer;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// tag::annotation-test[]
@SpringBootTest
@AutoConfigureMeilisearchContainer
class NotesServiceAnnotationTests {

    @Autowired
    NotesService notes;

    @Test
    void indexesAndSearchesNotes() throws Exception {
        notes.index(new Note("42", "Hello", "world"));

        List<Note> hits = notes.search("hello");

        assertThat(hits).extracting(Note::id).containsExactly("42");
    }
}
// end::annotation-test[]

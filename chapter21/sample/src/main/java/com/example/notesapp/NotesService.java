package com.example.notesapp;

import java.util.List;
import java.util.stream.Stream;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.json.GsonJsonHandler;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.TaskInfo;

import org.springframework.stereotype.Service;

@Service
public class NotesService {

    static final String INDEX_NAME = "notes";

    private final Client client;
    private final GsonJsonHandler json = new GsonJsonHandler();

    public NotesService(Client client) {
        this.client = client;
    }

    public void index(Note... notes) throws MeilisearchException {
        Index index = client.index(INDEX_NAME);
        TaskInfo task = index.addDocuments(json.encode(notes), "id");
        index.waitForTask(task.getTaskUid());
    }

    public List<Note> search(String query) throws MeilisearchException {
        SearchResult result = client.index(INDEX_NAME).search(query);
        return Stream.ofNullable(result.getHits()).flatMap(List::stream)
                .map(hit -> new Note(
                        String.valueOf(hit.get("id")),
                        String.valueOf(hit.get("title")),
                        String.valueOf(hit.get("body"))))
                .toList();
    }
}

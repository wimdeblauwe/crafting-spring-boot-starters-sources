# Crafting Spring Boot Starters — Example Code

Buildable example projects that accompany the book **Crafting Spring Boot Starters** by Wim Deblauwe.

Each top-level `chapterNN/` folder is a self-contained Maven project matching the chapter of the same number in the book.
A few chapters ship more than one variant in descriptively named sub-folders (for example, `chapter11/with-converter/` and `chapter11/without-converter/`).

## Requirements

- Java 21
- Maven 3.9+
- Spring Boot 4.0.6 (declared by the projects)

## Building a chapter

Each chapter builds on its own. From a chapter folder:

```
mvn install
```

Where a chapter includes a runnable `sample/` app, start it with:

```
mvn -f sample/pom.xml spring-boot:run
```


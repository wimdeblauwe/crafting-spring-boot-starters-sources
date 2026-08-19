# Crafting Spring Boot Starters — Example Code

Buildable example projects that accompany the book [Crafting Spring Boot Starters](https://www.wimdeblauwe.com/books/crafting-spring-boot-starters/) by [Wim Deblauwe](https://www.linkedin.com/in/wimdeblauwe/).

The book explains how to design, build, test and publish auto-configuration libraries on Spring Boot.

Each top-level `chapterNN/` folder is a self-contained Maven project matching the chapter of the same number in the book.
A few chapters ship more than one variant in descriptively named sub-folders (for example, `chapter11/with-converter/` and `chapter11/without-converter/`).

## Requirements

- Java 21
- Maven 3.9
- Spring Boot 4

## Building a chapter

Each chapter builds on its own. From a chapter folder:

```
mvn install
```

Where a chapter includes a runnable `sample/` app, start it with:

```
mvn -f sample/pom.xml spring-boot:run
```


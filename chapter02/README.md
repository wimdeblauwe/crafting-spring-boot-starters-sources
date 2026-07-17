# Chapter 2 — request-id starter

A minimal single-module Spring Boot starter, end to end.

## Layout

```
chapter02/
├── pom.xml                       starter POM
├── src/main/                     auto-configuration, properties, filter, imports file
└── sample/                       consumer app, built standalone
```

## Build and run

Install the starter into your local Maven repository, then run the sample:

```
mvn -f "pom.xml" install
mvn -f "sample/pom.xml" spring-boot:run
```

In another terminal:

```
curl -i http://localhost:8080/hello
curl -i -H "X-Request-Id: my-custom-id" http://localhost:8080/hello
```

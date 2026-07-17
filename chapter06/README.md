# Chapter 7 — Custom conditions

A custom `Condition` and its meta-annotation, demonstrated with a non-empty-map property check.

## Layout

```
chapter06/
├── pom.xml                       starter POM
├── src/main/                     properties, resolver, condition, meta-annotation, auto-config, imports file
└── sample/                       consumer that prints whether the conditional bean was registered
```

## Build and run

Install the starter into your local Maven repository, then run the sample:

```
mvn install
mvn -f sample/pom.xml spring-boot:run
```

With the sample's default `application.yaml` (two `header-overrides` entries), the
`HeaderOverrideResolver` bean is registered. The startup log shows:

```
INFO HeaderOverrideResolver registered with 2 override(s).
INFO HeaderOverrideResolver is registered with 2 entries.
```

To see the negative case, remove the `header-overrides` block from `application.yaml`
and re-run. The bean is not registered, and the log shows:

```
INFO HeaderOverrideResolver is NOT registered (condition did not match).
```

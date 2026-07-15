# Konsist Architecture Tests

Architecture and import validation for the KMP project. Runs as JUnit5 tests via `./gradlew :tools:linter:konsist:test`.

## Rules

| Test | What it forbids | Fix |
|------|----------------|-----|
| TestingAssertionsRule | `org.junit.Assert`, `org.hamcrest`, `kotlin.test.assert*` | Use `io.kotest.matchers.*` |
| TestingAnnotationsRule | `org.junit.Test/Before/After/Ignore` | Use `@kotlin.test.Test`, `@BeforeTest`, etc. |
| ProhibitMockLibrariesRule | MockK, Mockito, EasyMock, PowerMock | Write manual fakes |

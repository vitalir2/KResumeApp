# Custom Detekt Rules

Add custom Detekt rules here as a `kotlin("jvm")` Gradle submodule when needed.

## To add a new rule module

1. Create `build.gradle.kts` with `detekt-api` and `detekt-test` dependencies
2. Write rules under `src/main/kotlin/` referencing `io.gitlab.arturbosch.detekt.api.*`
3. Register in the parent `detekt.yml` under a custom rule set ID
4. Add tests under `src/test/kotlin/`

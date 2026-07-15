---
name: write-unit-tests
description: Write unit tests for KMP projects with correct patterns. Use when writing tests that run with `./gradlew test`.
---

Load this skill when writing KMP unit tests. Use for any test that runs on the JVM (`./gradlew test`).

## Test framework

Use `@kotlin.test.Test` annotations and kotest matchers for assertions. No mocking libraries — prefer fakes and stubs.

## Test structure (commonTest)

```kotlin
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MyUnitTest {
    @Test
    fun `description in backtick names`() {
        val result = myFunction()
        result shouldBe expectedValue
    }
}
```

### Unit test with coroutines

`runTest` only needed when testing suspend functions.

```kotlin
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class MyCoroutineTest {
    @Test
    fun `description in backtick names`() = runTest {
        val result = mySuspendFunction()
        result shouldBe expectedValue
    }
}
```

## kotest matchers table

| Assertion | Usage |
|-----------|-------|
| `shouldBe` | Equality check |
| `shouldNotBe` | Inequality |
| `shouldBeNull` / `shouldNotBeNull` | Null checks |
| `shouldBeTrue` / `shouldBeFalse` | Boolean assertions |
| `shouldThrow` | Exception expectation |
| `shouldContain` | Collection/string contains |
| `shouldHaveSize` | Collection size |

## Critical patterns

- **Use kotest assertions** (`shouldBe`, `shouldNotBe`) — not `kotlin.test.assertEquals` or `assertTrue` for equality
- **`runTest` only when testing coroutines** — plain tests don't need it
- **One test file at a time**: compile, run, verify before writing the next
- **Method names**: backtick names, lowercase + spaces, human-readable behavior
- **No mock libraries** — use fakes, stubs, or in-memory implementations
- **Tests in `commonTest`** for shared logic, platform-specific tests in `*Test` source sets
- **Test dependencies** go in `commonTest.dependencies` block via version catalog (`gradle/libs.versions.toml`)
- **Targeted edits, not full rewrites** — use `edit` with precise oldString/newString when updating existing test files

## Running

```bash
./gradlew test
```

## Reference

- `.ai/docs/testing-guide.md` — general testing guidelines
- `gradle/libs.versions.toml` — dependency catalog

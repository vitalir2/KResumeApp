# Code Style

This document describes the coding conventions for Kotlin projects using this template.

## Formatting

The project uses [ktlint](https://pinterest.github.io/ktlint/) with the `ktlint_official` style and experimental rules enabled. Key formatting settings:

- **Indentation**: 4 spaces (no tabs)
- **Maximum line length**: 120 characters
- **Line endings**: LF (Unix)
- **Final newline**: Required at end of every file
- **Trailing whitespace**: Trimmed automatically
- **Trailing commas**: Allowed on both declarations and call sites

Disabled ktlint rules:

- `function-naming`, `backing-property-naming`, `class-naming`, `filename`, `no-empty-file`, `no-wildcard-imports`

ktlint is disabled for the following paths: `generated/`, `*.bat`, `**/build/**`

### Pre-commit formatting

Run `./tools/formatter/format.sh` before committing to auto-format all Kotlin files.

## Imports

Use only the following test libraries:

- **Assertions**: `io.kotest.matchers.*` — all assertion functions come from Kotest matchers
- **Test annotations**: `kotlin.test.Test`, `kotlin.test.BeforeTest`, `kotlin.test.AfterTest`, `kotlin.test.Ignore`
- **Mock libraries**: **Forbidden** — do not use MockK, Mockito, EasyMock, or PowerMock. Prefer fakes and stubs over mocking.

### Migration table

If migrating from JUnit, replace old imports with their Kotlin equivalents:

| Old (JUnit) | New (Kotlin Test + Kotest) |
|-------------|---------------------------|
| `org.junit.Test` | `kotlin.test.Test` |
| `org.junit.Before` | `kotlin.test.BeforeTest` |
| `org.junit.After` | `kotlin.test.AfterTest` |
| `org.junit.Ignore` | `kotlin.test.Ignore` |
| `org.junit.Assert.*` | `io.kotest.matchers.*` |
| `org.hamcrest.*` | `io.kotest.matchers.*` |
| `kotlin.test.assert*` | `io.kotest.matchers.*` |

## Naming

Use simple, descriptive function names. For `@Composable` functions, prefer noun-based imperative names (e.g., `UserProfile`, `SettingsPanel`). Batch all imports at the top of the file — no inline imports.

## Structural rules

- **Function length**: Keep functions under 150 lines. If a function exceeds this, extract smaller helper functions.
- **Default arguments**: Avoid default parameter values unless explicitly requested as part of a public API contract.
- **Semicolons**: Never use semicolons (`;`) — Kotlin doesn't require them.
- **Branch braces**: Always use braces for branch bodies, even for single-line conditions. Never use single-line `if`/`when` without braces.
- **KDoc style**: Use `/** */` for documentation comments only. Never use `//` style comments for KDoc. File-level notes use `//`, API declarations use `/** */`.

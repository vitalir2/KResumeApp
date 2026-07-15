# kmp-general

KMP project template for any cross-platform Kotlin project: mobile apps, desktop apps, shared libraries, servers, games, CLI tools, etc.

## Getting Started

```bash
./setup.sh                    # Install ktlint, detekt, configure hooks
./gradlew build               # Compile all targets
./gradlew test                # Run shared unit tests
```

## Targets

| Target | Source Set | Entry Point |
|--------|-----------|-------------|
| Android | `androidMain/` | `MainActivity.kt` |
| iOS | `iosMain/` | `MainViewController.kt` |
| JVM/Desktop | `jvmMain/` | `Main.kt` (`fun main()`) |

## Architecture

```
commonMain/          Shared UI + domain (Compose Multiplatform)
jvmMain/             Desktop entry point
commonTest/          Cross-platform unit tests (kotest)
```

## Key Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| UI Framework | Compose Multiplatform | Shared UI across all platforms |
| Serialization | kotlinx-serialization | KMP-native, compile-time safe |
| Async | kotlinx-coroutines | KMP, structured concurrency |
| Assertions | kotest matchers | Descriptive errors, no mock libs |
| Platform abstraction | Interface in commonMain | Testable, explicit API surface |
| DI | Manual constructor injection | No framework overhead |

## Extending

- **New screen**: add `@Composable` in `commonMain/`, wire platform entry point.
- **New platform target**: add to `kotlin { }` block + source set + platform actuals.
- **New `.ai/tools/` script**: create under `.opencode/skills/` with YAML frontmatter.

## More Docs

| Path | Content |
|------|---------|
| `.ai/docs/` | AI-optimized: code style, API guidelines, conventions |
| `docs/` | Human-readable counterparts |
| `README.md` per module | Purpose, type, public API for each module |

# KResume

I'm building a Developer-First Resume IDE — a desktop application for
developers who treat their CV as code. Still in progress, but the end
product will combine a type-safe Kotlin DSL with a live preview, all
powered by Compose Multiplatform. See [docs/idea.md](docs/idea.md) for
the full feature set and vision.

## Features (Goal)

- **Kotlin DSL** — type-safe, expressive DSL for resume data
- **Live preview** — instant feedback as you write
- **Import / Export** — multiple formats (JSON, Markdown, HTML, PDF)
- **Private & offline** — all processing on your machine
- **AI assistant** — optional, for rewording and optimisation

## Getting Started

```bash
./setup.sh                    # Install ktlint, detekt, configure hooks
./gradlew build               # Compile all targets
./gradlew test                # Run shared unit tests
```

## Architecture

```
commonMain/          DSL core, resume model, preview engine
jvmMain/             Desktop entry point (JVM)
commonTest/          Cross-platform unit tests (kotest)
```

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI Framework | Compose Multiplatform (Desktop) |

## More Docs

| Path | Content |
|------|---------|
| `.ai/docs/` | AI-optimized: code style, API guidelines, conventions |
| `docs/` | Human-readable counterparts |
| `docs/idea.md` | Full product vision and roadmap |

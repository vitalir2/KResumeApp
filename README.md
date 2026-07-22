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

## PoC: Runtime Compilation Server (2026-07)

A functional proof-of-concept: a local web server (`:server` module)
with a browser-based IDE that compiles Kotlin DSL to an interactive
preview. Used to evaluate whether runtime Kotlin compilation is fast
enough for a sub-second hot-reload experience.

### Run it

```bash
./gradlew :server:run                          # default engine: raw
./gradlew :server:run --args="--engine=jsr223"  # JSR 223 scripting
./gradlew :server:run --args="--engine=kctfork" # kctfork compiler
./gradlew :server:run --args="--engine=raw"     # kotlin-compiler-embeddable
```

Open [http://localhost:8080](http://localhost:8080) — type DSL in the left
pane, see the rendered resume update live (~200ms debounce).

### What's inside

| Path | Content |
|------|---------|
| `server/src/main/kotlin/.../model/` | `Resume`, `Basics`, `WorkEntry` — all nullable, serializable |
| `server/src/main/kotlin/.../dsl/` | Builder DSL: `resume { basics { } work { } }` |
| `server/src/main/kotlin/.../compiler/` | 3 backends: JSR 223, kctfork, raw (common `ResumeCompiler` interface) |
| `server/src/main/kotlin/.../handler/` | `POST /api/compile`, `/api/bench`, `/api/export` |
| `server/src/main/kotlin/.../render/` | HTML preview (ATS-compatible) + PDF export (OpenHTMLtoPDF) |
| `server/src/main/resources/static/` | CodeMirror 5 IDE, style, JS, benchmark page |
| `server/src/main/resources/bench/` | 50/200/500-line DSL snippets for benchmarking |

### Benchmark page

Navigate to `/bench` (or click "Benchmark" in the toolbar).
Tests all 3 engines across 3 resume sizes — cold + hot metrics,
side-by-side comparison chart with metric selector.

### Results (cold, first compile)

| Engine | ~Cold start | Hot P50 |
|--------|------------|---------|
| jsr223 | 2000ms* | ~110ms |
| kctfork | 1400ms* | ~110ms |
| raw | **100ms** | **~90ms** |

\* First JVM compilation includes JIT warmup. Subsequent compiles
are stable within 100-200ms.

## Getting Started

```bash
./setup.sh                    # Install ktlint, detekt, configure hooks
./gradlew build               # Compile all targets
./gradlew test                # Run shared unit tests
```

## Architecture (future)

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
| Web (PoC) | Ktor 3.5.1 Netty, CodeMirror 5 |

## More Docs

| Path | Content |
|------|---------|
| `.ai/docs/` | AI-optimized: code style, API guidelines, conventions |
| `docs/` | Human-readable counterparts |
| `docs/idea.md` | Full product vision and roadmap |
| `docs/ats-compatibility.md` | ATS parsing rules, checks, and testing guide |
| `history/brainstorms/` | Brainstorm sessions (PoC design, ADR process) |

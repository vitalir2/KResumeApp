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

All times in milliseconds. Raw is consistently ~2x faster and wins on
every metric with **no extra third-party dependency**.

#### Size: 50 lines

| Metric | jsr223 | kctfork | raw |
|--------|--------|---------|-----|
| Cold (ms) | 312 | 257 | **72** |
| P50 (ms) | 97.0 | 78.0 | **67.9** |
| P95 (ms) | 159.7 | 133.9 | **77.0** |
| P99 (ms) | 175.7 | 156.0 | **85.4** |
| Mean (ms) | 103.7 | 87.2 | **68.7** |

#### Size: 200 lines

| Metric | jsr223 | kctfork | raw |
|--------|--------|---------|-----|
| Cold (ms) | 103 | 92 | **85** |
| P50 (ms) | 95.1 | 93.5 | **77.5** |
| P95 (ms) | 150.3 | 108.7 | **84.8** |
| P99 (ms) | 182.7 | 188.4 | **131.7** |
| Mean (ms) | 105.7 | 97.6 | **80.1** |

#### Size: 500 lines

| Metric | jsr223 | kctfork | raw |
|--------|--------|---------|-----|
| Cold (ms) | 145 | 119 | **102** |
| P50 (ms) | 136.9 | 107.2 | **101.3** |
| P95 (ms) | 192.7 | 120.6 | **110.8** |
| P99 (ms) | 222.0 | 166.7 | **115.1** |
| Mean (ms) | 148.2 | 111.6 | **103.5** |
| StdDev | 24.1 | 13.9 | **5.7** |

Raw wins on every metric — less variance too (StdDev 5.7 vs 13.9-24.1).
That's why it's the default engine.

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

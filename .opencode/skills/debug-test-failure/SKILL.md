---
name: debug-test-failure
description: Systematic layered debugging for test failures. Use when a test fails and the cause is not obvious from the failure message.
---

# Debug Test Failure

## Trigger
A test fails and the root cause is unclear after reading the failure message.

## Layered debugging workflow

Work through layers sequentially. Each layer answers a specific question.

### Layer 0 — Hypothesis
Before running the test, state **what you expect** and **3+ failure modes** (what could go wrong). This frames the investigation and prevents random guessing.

### Layer 1 — Signal
Read the build output:
- `BUILD SUCCESSFUL` or `BUILD FAILED`
- Test count and failure count
- Failed test names

### Layer 2 — Details
Read the failed test assertion error messages and stack traces from the build output.

### Layer 3 — Context
Read the full Gradle output with complete stacktraces. Look for actual error message lines around the FAILED marker.

### Layer 4 — Capture
Add diagnostic output at key checkpoints in the test or production code. Use `println` or log statements to capture intermediate values:

```kotlin
val parsed = SourceParser.parse(input)
println("parser output: ${parsed.sections}")
val rendered = Renderer.render(parsed.sections)
println("roundtrip match: ${input == rendered}")
```

### Layer 5 — Probing techniques

| Technique | How | When to use |
|-----------|-----|-------------|
| **Throw probe** | `throw RuntimeException("here")` in a callback/method | Unsure if a code path is reached |
| **Hardcode return** | Replace method body with `return "fixed-value"` | Isolate which step breaks the pipeline |
| **Binary search** | Comment out half the steps, check if test still fails | Large multi-step test — narrow to the exact assertion |
| **Assertion sandwich** | Add `assertTrue(true)` before/after the suspect assertion | Stack trace truncation — verify WHICH assertion fails |
| **Negative test** | Assert the opposite condition | Confirm your assumption about the data is wrong |
| **Input capture** | `println("input: $value")` at function entry | Function processes input incorrectly |
| **Output capture** | `println("output: $result")` after function call | Wrong output from correct input |
| **Pipeline trace** | `println("step 1 done")` at each transformation | Multi-step pipeline — find which step corrupts data |

### Layer 6 — Fix
Once probing identifies the exact cause, apply the fix in **one attempt**. If the fix fails, return to Layer 4 — do not iterate on guesses. After 5 failed fix attempts → write isolation test proving ONE assumption. Still stuck → ask.

## Tools
- Gradle test output in console
- `println` / logger diagnostics at capture points
- IDE debugger for stepping through code

---
name: debug-kotlin-exception
description: Diagnose Kotlin Multiplatform runtime exceptions that compiled successfully. Use for compile-but-crash patterns: missing compiler plugins, expect/actual mismatches, missing runtime dependencies.
---

# Debug Kotlin Exception

## Trigger
A Kotlin KMP project compiles but crashes at runtime. The crash is not in business logic — it's infrastructure (missing class, missing method, no actual declaration).

## Diagnostic table

| Symptom | Likely cause | Check | Fix |
|---|---|---|---|
| `NoSuchMethodError` on `@Serializable` class | Missing `kotlin("plugin.serialization")` in module `plugins {}` | grep plugins in build.gradle.kts | Add `kotlin("plugin.serialization")` |
| `NoSuchMethodError` on `expect fun` / `expect class` | Missing `actual` declaration in target source set | Check `commonMain` expect → verify `actual` exists in each `*Main` | Add missing actual |
| `ClassNotFoundException` at runtime | Dep is `compileOnly` or `api` without transitive | Check gradle dep scope in consumer module | Change to `implementation` |
| `NoSuchMethodError` on KSP-generated class | Missing KSP plugin or processor | grep `ksp(` in build.gradle.kts | Add KSP + processor dep |

## Procedure

1. Get the full stack trace (logcat or exception message).
2. Identify the failing class/method — is it a generated class, framework class, or user class?
3. Check pattern 1 (serialization): look for `@Serializable` in stack → check plugins.
4. Check pattern 2 (expect/actual): look for expect keywords → check source set layout.
5. Check pattern 3 (runtime deps): look for `ClassNotFoundException` → check dependency scope.
6. If none match, escalate to standard debugging.

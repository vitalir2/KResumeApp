# API Writing Guidelines

This document describes how to write clear, consistent API documentation for Kotlin declarations.

## Visibility → KDoc Requirements

The level of documentation required depends on visibility:

| Visibility | KDoc Required? |
|------------|---------------|
| `public` / `protected` | **Required** — every public and protected declaration must have KDoc |
| `internal` / `private` | **Use judgment** — document if the implementation is non-obvious |
| `@PublishedApi internal` | **Required** — these are effectively public API |

### Edge cases requiring KDoc

| Case | Convention |
|------|------------|
| **Data classes** | Class-level KDoc + `@property` for each constructor parameter |
| **Sealed classes / interfaces** | Interface KDoc plus KDoc on each variant |
| **Enum constants** | Document each constant individually |
| **Composable functions** | Describe what the composable displays and any side effects |
| **Protected members** | Document the full contract for subclasses |
| **Extension functions / properties** | Document the receiver type and the behavior being added |
| **Test infrastructure** | Document purpose and test setup expectations |
| **Public typealiases** | Document why the alias was created |
| **`expect` declarations** | All `expect` declarations across platforms must have KDoc |

### What to cover in KDoc

Every KDoc should cover:

- **Purpose**: What this declaration does
- **Threading**: Whether it's thread-safe and what threading model it uses
- **Errors**: What errors/exceptions can be thrown and under what conditions

## Documentation Placement

| Documentation | Location | Purpose |
|--------------|----------|---------|
| KDoc | On the declaration | How to call the API |
| External docs | `README.md` | Why the API exists, architecture decisions |
| Cross-references | `@see README.md` on domain-level APIs only | Link to higher-level docs |

**No duplication**: Don't repeat information between KDoc and external README docs. Each piece of documentation has a single source of truth.

## General Rules

1. **No mixed comment styles**: Never use both `/** */` and `//` on the same declaration. File-level notes use `//`, declaration documentation uses `/** */`.
2. **English only**: All documentation must be in English.
3. **KDoc tag usage**:
   - `@property` — for constructor parameters
   - `@receiver` — for extension functions/properties
   - `@throws` — when the `@Throws` annotation is present
   - **Deprecation** — use the `@Deprecated` annotation, not KDoc comments

## Module README

Every module in the project should have a `README.md` containing:

- `## Purpose` — why this module exists
- `## Module Type` — what kind of module (e.g., library, feature, app, tool)
- `## Public API` — table of public declarations with brief descriptions

### Module `.ai.readme.md`

Alongside each `README.md`, create an `.ai.readme.md` with:

- **Frontmatter**: `type: both`, `synced_at: sha256:<hash of README.md>`
- **Content**: Property table (Module, Gradle path, Type, Purpose, Key classes, Dependencies) plus optional API table
- **Regenerate** when the human `README.md` changes — update the `synced_at` hash

## Script Header

Standalone CLI scripts (under `tools/`) must include a header comment in the first 30 lines:

```
# What:      brief purpose statement
# Why:       motivation for the script's existence
# Usage:     command-line usage with arguments
```

For TypeScript executables, use `//` comments. For TypeScript modules, use JSDoc.

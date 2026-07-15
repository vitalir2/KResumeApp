# Documentation Conventions

This document describes how to write effective documentation for the project. It covers format selection, the frontmatter type system, and module documentation conventions.

## Format Hierarchy

When writing documentation, prefer the most efficient format for the content:

| Format | When to Use | Efficiency |
|--------|------------|------------|
| **Table** | Structured comparisons (parameter lists, rules, types) | Best |
| **Bullet list** | Sequential rules, unordered items | Good |
| **Numbered list** | Ordered steps | OK |
| **Short prose** | Narrative explanations (use sparingly) | Poor |
| **Code block** | Only when essential — prefer inline `code` | Worst |

**Golden rule**: Before writing any documentation, ask yourself "can this be a table?" If yes, use a table instead of prose.

## Writing Rules

1. **Tables over prose** — they pack more information per line
2. **No introductions** — the filename IS the introduction. Skip phrases like "This document explains..."
3. **Fragments are OK** — use bullet points, not full paragraphs
4. **One rule per line** — never bury multiple rules in a single paragraph
5. **No obvious examples** — only provide examples when the rule could be ambiguous
6. **No code quotes** — reference the file or function by path/name instead of copying code verbatim
7. **Cross-reference, don't copy** — write information once and reference it elsewhere
8. **Don't restate the type system** — the compiler already guarantees types

## Frontmatter Type System

Every documentation file MUST have YAML frontmatter with a `type` field:

| Type | Meaning | Location |
|------|---------|----------|
| `type: ai` | AI operational instructions only | `.ai/docs/` only |
| `type: human` | Human-readable documentation only | `docs/` only |
| `type: both` | Shared between `docs/` and `.ai/docs/` | Both directories at the same relative path |

Files with `type: both` also require a `synced_at` field to track synchronization:

```yaml
---
type: both
synced_at: sha256:<hex_hash_of_human_version>
---
```

The hash is SHA-256 of the human-readable version (frontmatter stripped). This allows automated tools to detect when one side is out of date.

## Module `.ai.readme.md`

Every module with a `README.md` should have an `.ai.readme.md` file alongside it in the same directory. This is the AI-optimized compact counterpart:

- **Frontmatter**: `type: both`, `synced_at: sha256:<hash of human README.md>`
- **Content**: A property table (Module, Gradle path, Type, Purpose, Key classes, Dependencies) and an optional API table
- **Regenerate** when the human `README.md` changes — update the `synced_at` hash to reflect the new content

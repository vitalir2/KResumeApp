---
type: both
synced_at: sha256:9188f6b151b85b774e19e7bdf906cb25baa20a4e02d9b6e3e1fe4fcf3418a0af
---
# AI Doc Writing (compact-first)

## Format hierarchy (most → least efficient)
| Format | When to use | ~Tokens |
|--------|------------|---------|
| Table | Structured comparison (params, rules, types) | best |
| Bullet list | Sequential rules, unordered items | good |
| Numbered list | Ordered steps | ok |
| Short prose | Narrative explanation (rare) | poor |
| Code block | Only when essential | worst — prefer inline `code` |

## Rules
- **Tables over prose** — pack more info per token
- **No intros** — filename IS the intro. Skip "This doc explains..."
- **Fragments OK** — bullets, not paragraphs
- **One rule per line** — never bury in paragraph
- **No obvious examples** — only when rule is ambiguous
- **No code quotes** — reference file/function, don't copy
- **Cross-ref, don't copy** — write once, reference elsewhere
- **No restating type system** — compiler already guarantees types

Before writing: "can this be a table?" If yes, don't use prose.

## Frontmatter type system
Every doc file MUST have YAML frontmatter: `type: ai|human|both`.
- `type: both` — shared between `docs/` and `.ai/docs/` at same relative path
- `type: ai` — exists only in `.ai/docs/` (AI operational instructions)
- `type: human` — exists only in `docs/` (archive/historical)

`type: both` also requires `synced_at: sha256:<hex>` — hash of human version content.

## Module .ai.readme.md
Every module with `README.md` should have `.ai.readme.md` alongside it. Frontmatter: `type: both`, `synced_at` hash.

---
type: both
synced_at: sha256:dbed8a54ecbf47db46166de0e6667d4fd2ee20118db18c5af4c53d5957055147
---
# API Writing Rules

## Visibility → KDoc Gate

| Visibility | KDoc |
|---|---|
| `public`/`protected` | Required |
| `internal`/`private` | Judgment |
| `@PublishedApi internal` | Required |

## KDoc Required For

All public declarations. Edge cases:

| Case | Convention |
|------|------------|
| Data classes | Class KDoc + `@property` per param |
| Sealed class | Interface KDoc + each variant |
| Enum constants | Each constant individually |
| Composable screens | Describe display + side effects |
| Protected members | Full subclass contract |
| Extension functions/props | Document receiver type + added behavior |
| Test infrastructure (all) | Document purpose + test setup expectations |
| Typealiases (public) | Document the alias decision |
| `expect` decls | All `expect` declarations |

Cover: purpose, threading, errors.

## Documentation Placement

| Layer | Where |
|-------|-------|
| KDoc | On declaration — how to call |
| External docs | `README.md` — why it exists |
| Cross-ref | `@see docs/path.md` on domain-level APIs only |

No duplication between KDoc and external docs.

## Rules

- No `/** */` + `//` mix on same decl. File=`//`, decl=`/** */`.
- English only.
- `@property` for constructor params, `@receiver` for extensions, `@throws` when `@Throws`, deprecation in annotation not KDoc.

## Module README

Every module should have `README.md` with: `## Purpose`, `## Module Type`, `## Public API` (table).

## Module `.ai.readme.md`

Every module with `README.md` should have `.ai.readme.md` alongside it. Frontmatter: `type: both`, `synced_at: sha256:<hash>`. Content: property table + optional API table.

## Script Header (first 30 lines)

```
# What:      purpose
# Why:       motivation
# Usage:     args
```

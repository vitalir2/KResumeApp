---
name: sync-documentation
description: Keep `.ai/docs/` and `docs/` in sync (bidirectional, judgmental expansion/compaction). Use when making ANY documentation changes AND at end of session / before committing.
---

# Sync Documentation

**AI docs must follow `.ai/docs/doc-conventions.md`** — write compact from the start (tables > bullets > prose, no intros, fragments OK).

## Frontmatter type system

Every doc file in `.ai/docs/` MUST have frontmatter with a `type` field:
- `type: ai` — AI-only operational rules. Never synced to `docs/`.
- `type: both` — Relevant to both AI and humans. Synced bidirectionally. Both copies must exist.
- `type: human` — Human-only (rare).

Files without frontmatter default to `type: both` for backward compatibility, but audit will flag them.

## Hash-based staleness tracking

Each synced file carries a `synced_at: sha256:<hash>` field in its frontmatter. Compare content hashes (stripping frontmatter) to detect staleness.

**Do not edit `synced_at` manually.** After syncing doc content, run `shasum -a 256 <file>` over the human version (frontmatter stripped) to compute the new hash, then update the AI copy's frontmatter.

## Direction: `docs/` → `.ai/docs/`

Human docs are authoritative for human-relevant content. Compact them for AI consumption:

1. **Audit** — For each doc file in `docs/`, check if `.ai/docs/<file>` exists and compare hashes:
   - All synced → nothing to do
   - Missing or stale → proceed

2. **Compact** — For each file needing sync:
   - Read `docs/<file>`
   - Apply `doc-conventions.md` rules: tables > bullets > prose, no intros, fragments OK
   - Strip: articles, filler, pleasantries, verbose examples, introductory/transitional text
   - Keep: all rules, constraints, conventions, package paths, interface shapes, API signatures
   - Do NOT change meaning — just reduce token count
   - Write to `.ai/docs/<file>` with frontmatter `type: both` and no `synced_at`

3. **Fix hashes** — For each synced file, compute hash of human version (frontmatter stripped) via `shasum -a 256`, then update `synced_at` in the `.ai/docs/` copy.

4. **Verify** — Re-check that `.ai/docs/` and `docs/` pairs have matching content (frontmatter-stripped) and hashes are current.

## Direction: `.ai/docs/` → `docs/`

Only files with `type: both` (or no frontmatter) are candidates for syncing to `docs/`. Skip `type: ai` files entirely.

For each `.ai/docs/` file with a `docs/` counterpart that is stale (hash mismatch):

1. Read `.ai/docs/<file>` — the compact AI version
2. Expand into human-readable form in `docs/<file>`:
   - Add prose, explanations, context, examples
   - Restructure for readability (headings, tables, code blocks)
   - Keep all rules, conventions, and APIs — don't lose substance
   - Remove AI-only operational rules (skill-loading instructions, tool call patterns, agent guidelines)
3. If no `docs/` counterpart exists AND the content is human-relevant, create one

## Module README sync

Every module directory that has a `README.md` should also have a companion `.ai.readme.md` in the same directory. The `.ai.readme.md` is the AI-compacted version:

- `module/README.md` ↔ `module/.ai.readme.md` (same directory, not in `.ai/docs/`)
- Check `.ai.readme.md` pairs via `find . -name '.ai.readme.md'` — verify `synced_at` against `README.md` content hash

## Batch sync rules

When syncing 3+ files:
1. **Group by build** — Batch all stale files from one sync direction together.
2. **Process each file** — Read, compact/expand, write, update hash.
3. **Consolidate** — Commit all doc changes in one batch.

**Never commit individual file syncs.** Batch commits only.

## When to load

- Before editing any file in `.ai/docs/` or `docs/`
- After a git pull/branch switch that updated documentation
- When the user says "sync docs" or mentions documentation being out of date
- **Before committing** — after any code changes that added knowledge
- **At end of session** — ensure all discoveries are documented

## Verification

- Ensure `docs/<file>` and `.ai/docs/<file>` have matching content (frontmatter-stripped)
- Verify `synced_at` hashes are current for all `type: both` files
- Temp files go to `.ai/tmp/`, never `/tmp/`

---
name: compact-context
description: Compact AI context files. Use when asked to optimize AGENTS.md or any instruction/config file.
---

# Context Compaction

Reduce AI context to minimal tokens while preserving all technical substance.

## Principles

1. **Preserve intent** — Never remove rules, only simplify prose without changing meaning. MUST NOT change meaning even if obvious. If rule contains "explicitly", keep verbatim.

2. **Split by role** — Sections specific to feature work, tooling → extract to lazy-loaded files. Only universal rules stay in main file.

3. **Lazy-load refs** — Package maps, API docs, verbose rule sets → separate files. Main gets one-liner: *"Read X.md when doing Y"*.

4. **Compact prose** — Remove articles, filler, hedging, pleasantries. Short synonyms. Fragments OK.

5. **Remove obvious** — Build commands, trivial conventions user already knows. If they'd never ask, cut it. Do NOT change meaning.

6. **Cap growing files** — Error logs, mistake lists → hard cap 15 lines. Trim old entries.

7. **No content quoting** — State what changed, don't quote.

8. **Do NOT change meaning** — Never rephrase altering intent. Never "improve" beyond compression. If unsure, keep original.

## Files to NEVER touch
- `.ai/usage-guide.md` — ignore
- `.ai/self-feedback.md` — ignore

## Process

1. **Read target file** — full content.
2. **Categorize** each line/block:
   - **Universal** — stays in main
   - **Role-specific** → `docs/<role>-guide.md`
   - **Reference data** → separate file, add lazy-load note
   - **Obvious** → delete
3. **Compact** — Only if predicted saving ≥ 500 bytes:
   - Compact prose (caveman rules).
   - Skip if already compact enough.
4. **Offer options** — user decides:
   - **Apply (1st)** — save compacted version
   - **Reject (2nd)** — revert
   Never delete backups or commit automatically.

### Multi-file compaction
3+ files → process each independently. Each file compacted and reported separately.

## Expected savings

| Technique | Typical |
|-----------|---------|
| Split by role | 20-30% |
| Lazy-load refs | 15-25% |
| Compact prose | 15-25% |
| Remove obvious | 5-10% |
| Cap growing files | variable |
| **Combined** | **50-70%** |

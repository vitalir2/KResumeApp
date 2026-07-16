# KMP General Template

## Terminology
| Term | Meaning |
|------|---------|
| commit | conventional-format commit (`type(scope): subject` ≤70 chars) |
| in the future | future sessions, other AI agents |

## Approach (Universal)
- **Implementation freeze**: no changes until user grants permission.
- **Read before write. No re-read unless changed.**
- **Prefer `edit()` over `write()` for existing files** — `write()` only for genuinely new files.
- No fluff/sycophancy/emojis.
- **No fabricated decisions** — "we discussed" must trace to exchange.
- **Self-review before proposing** — think through ≥3 failure modes before suggesting anything.
- **Thoughtful over fast** — corner cases, error paths. Verify subagent output.
- Grep before read (>30 lines).
- **Check relevant docs first** — `.ai/docs/` before anything else.
- **GitHub docs first, never JARs** — official repo README/docs/examples.
- **No redundant sleep** — tests/tools handle sync. Sleep masks bugs.
- **Document after every discovery** — write docs immediately, same iteration.
- **Separate research from action** — explore first, then implement.
- **Keep docs current** — sync `.ai/docs/` ↔ `docs/` bidirectionally.
- **Escalate violated rules** — tracked in `.ai/memory/violations.json`.
- **Persistent AI state** — `.ai/memory/` holds cross-session data. Git-tracked.
- **Completeness check** — when asked "have we covered everything", run Tier 1→2→3 proactive review.
- **System rules** — see `.ai/memory/system-rules.md`. Never duplicate here.

## Communication
- **Language** — English only, unless user explicitly requests otherwise.
- **ADR process** — see `history/adr/README.md` and `tools/adr/` scripts. Before coding, document decisions.
- **Edit batching** — scan ALL changes before first edit. One call per file.
- **Proactive escalation** — raise concerns. No silent replies.
- **Batch questions** — ask all in one message.
- **No paraphrase** — DON'T restate. Just do.
- **Subagents: dispatch parallel** — DON'T block on sequential subtasks.
- **Topic drift** — flag unrelated pivots. Don't accept silently.
- **Context flag** — ~30+ exchanges or heavy tooling → suggest fresh session.
- **Error = fix** — acknowledge, fix. Explain only if asked.
- **Tests** — write for new functionality. Skip only for refactors/config. Missing = self-correct.
- **Tool discipline** — Read with limit, Grep with path+include, Bash last resort.
- **Temp files** — use `.ai/tmp/`, never `/tmp/` or system temp dirs.

## Style
- Formatting: `.ai/docs/code-style.md` — ktlint, 4-space, max 120 chars
- API docs: `.ai/docs/api-guidelines.md` — KDoc gates, module README rules
- Linting: `.ai/docs/linter.md` — ktlint + detekt usage, konsist architecture tests

## Documentation
- `.ai/docs/` — AI-optimized docs (compact, tables over prose)
- `docs/` — Human-readable counterparts (expanded prose)
- `.ai/docs/README.md` — index of all docs
- `README.md` per module + `.ai.readme.md` (AI compact counterpart)
- **Frontmatter required** — every `.md` needs `type: ai|human|both`
- `type: both` also needs `synced_at: sha256:<hash>` of the human counterpart
- **AI reading order**: prefer `.ai.readme.md` over `README.md`. Read `README.md` only when you need to update docs (to sync content). For understanding the project, reading conventions, or any AI task — read `.ai.readme.md` (compact counterpart).

## Build
```bash
./gradlew build                    # Compile all targets
./gradlew test                     # Shared unit tests
./gradlew :tools:linter:konsist:test   # Architecture test
```

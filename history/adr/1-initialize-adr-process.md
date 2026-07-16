# Initialize ADR Process

**Status:** implemented
**Date:** 2026-07-16 (updated 2026-07-16 — full process finalized)

## Context and Problem Statement

The project needs a structured way to capture architectural decisions.
Without records, rationale is lost — future developers (and AI agents)
don't know why things were done a certain way. The project also serves
as a professional resume artefact, so demonstrating structured
decision-making is valuable.

Two additional concerns emerged during design:
- The process must be AI-friendly — AI agents should be able to create
  and maintain ADRs as easily as humans
- The overhead must be minimal — too many steps discourage usage

## Decision Drivers

- Knowledge retention across sessions and contributors
- Resume signal: show structured architectural thinking
- AI-friendly: decisions must be machine-readable and easy to act on
- Low friction: process must not discourage ADR creation

## Considered Options

### Template format

1. **No formal ADR** — decisions live in commit messages and PR comments
2. **Nygard template** — minimal: Context, Decision, Consequences
3. **MADR template** — full: Context, Drivers, Options with pros/cons,
   Decision, Consequences, Links

### Lifecycle

1. **No lifecycle** — single status, no state machine
2. **Two-status** — accepted → implemented
3. **Four-status** — proposed → accepted → implemented → deprecated

### Go/no-go gates

1. **No gates** — create ADR directly
2. **Light gates** — significance check only
3. **Structured gates** — significance + readiness + evidence + quality

### Decision tools

1. **Manual only** — write ADR files by hand
2. **Scripts only** — bash scripts for creation and status
3. **Scripts + AI skill** — scripts plus dedicated AI skill with protocol

### File naming

1. **No convention** — freeform filenames
2. **Zero-padded digits** — `001-adr-slug.md`
3. **Plain digits without `-adr-`** — `1-slug.md`

## Decision Outcome

**Chosen:** MADR template (simplified) with full supporting process.

### Template format: MADR (extracted to template.md)

The canonical template lives in `history/adr/template.md` and is copied
by `tools/adr/new.sh`. Sections: Context, Decision Drivers, Options,
Decision Outcome (with optional Confidence), Consequences, Pros/Cons,
More Information. The section originally called "Links" was renamed
to "More Information" to accommodate broader context (external refs,
follow-up decisions, assumptions).

### Lifecycle: proposed → accepted → implemented → deprecated

| Status | Meaning |
|--------|---------|
| `proposed` | Under discussion, not finalized |
| `accepted` | Decision finalized, ready to implement |
| `implemented` | Code matching the decision is merged |
| `deprecated` | Superseded by a newer ADR |

The lifecycle is enforced by `tools/adr/status.sh`, which validates
transitions and rejects invalid ones.

### Go/no-go gates: four-stage process

Before an ADR reaches `accepted`, it passes through:
1. **ASR test** (significance) — ≥3 of 5 criteria must be met, or
   user confirms tactical decision is worth documenting
2. **START check** (readiness) — stakeholders, timing, alternatives,
   requirements, template
3. **Evidence gate** — research, past experience, or POC validates
   the chosen option will work (with AI research fallback)
4. **Quality checklist** — 6 checks: context, alternatives, pros/cons,
   requirements-based justification, completeness, references

### Decision tools: scripts + AI skill

Three bash scripts:
- `tools/adr/new.sh` — creates an ADR from `template.md`, Status=proposed
- `tools/adr/status.sh` — transitions lifecycle status
- `tools/adr/index.sh` — regenerates `INDEX.md` (auto-run by new.sh and status.sh)
- `tools/brainstorms/index.sh` — regenerates brainstorms INDEX.md

One AI skill: `.opencode/skills/create-adr/SKILL.md` — full protocol
for AI-assisted ADR creation including exploration, gates, and finalization.

One integration point: the brainstorm-explore skill can create ADR drafts
from brainstorm artifacts, starting as proposed and awaiting finalization.

### File naming: plain digits

Files are named `history/adr/<NNN>-<slug>.md` where NNN is a sequential
positive integer starting at 1. No zero-padding, no redundant `-adr-`
infix. Index is maintained in a separate `INDEX.md` (not inline in README).

### Positive Consequences

- Clear traceability of all decisions and their rationale
- Easy for AI to consume and create ADRs
- Low friction — scripts automate boilerplate
- Structured gates prevent poorly-justified decisions
- First ADR bootstraps the process itself
- Resume signal: demonstrates methodical architectural thinking

### Negative Consequences

- Small overhead before coding (writing the ADR first)
- Four-stage gate process adds steps before finalizing
- Must remember to update status when implemented/deprecated
- Scripts require maintenance if template changes

## Pros and Cons of the Options

### Template format

#### No formal ADR

- **Good,** because zero overhead
- **Bad,** because knowledge is lost across sessions
- **Bad,** because resume signal is absent

#### Nygard template

- **Good,** because simple and proven (Context, Decision, Consequences)
- **Good,** because very quick to write
- **Bad,** because no explicit alternatives section — "why not X" is buried in prose
- **Bad,** because no way to link related decisions

#### MADR template — chosen

- **Good,** because explicit alternatives with pros/cons per option
- **Good,** because Links/More Information section connects decisions into a graph
- **Good,** because widely adopted standard with tooling support
- **Good,** because easy to extract into a standalone `template.md`
- **Bad,** because more sections to fill than Nygard

### Lifecycle

#### No lifecycle (single status)

- **Good,** because simplest possible model
- **Bad,** because cannot distinguish "discussing" from "done" from "replaced"
- **Bad,** because no trail of decision evolution

#### Two-status (accepted → implemented)

- **Good,** because simple: planned vs done
- **Bad,** because no room for discussion before committing (no proposed state)
- **Bad,** because no way to mark a decision as abandoned or replaced (no deprecated)

#### Four-status (proposed → accepted → implemented → deprecated) — chosen

- **Good,** because `proposed` captures the discussion phase before committing
- **Good,** because `deprecated` preserves history instead of deleting
- **Good,** because enforced by `status.sh` with clear transition rules
- **Bad,** because more statuses = more maintenance (must remember to update)
- **Bad,** because `proposed` files are uncommitted — risk of losing local changes

### Go/no-go gates

#### No gates (create ADR directly)

- **Good,** because fastest path from idea to document
- **Bad,** because no quality filter — shallow decisions get documented as-is
- **Bad,** because AI might create ADRs for non-architectural topics

#### Light gates (significance check only)

- **Good,** because filters out tactical noise without slowing down real decisions
- **Bad,** because doesn't verify evidence or readiness — still get poorly-supported decisions

#### Structured gates (ASR → START → evidence → quality) — chosen

- **Good,** because four-stage filter ensures decisions are significant, ready, evidenced, and complete
- **Good,** because each gate is optional — user can bypass with confirmation
- **Good,** because AI handles the gates automatically via the skill
- **Bad,** because adds steps before finalizing (can feel bureaucratic)

### Decision tools

#### Manual only (write ADR files by hand)

- **Good,** because zero setup, just a text editor
- **Good,** because no scripts to maintain
- **Bad,** because no status validation — easy to make illegal transitions
- **Bad,** because index must be updated manually
- **Bad,** because template must be copy-pasted every time

#### Scripts only (bash tools without AI skill)

- **Good,** because automates creation, status transitions, and index
- **Good,** because enforces valid lifecycle transitions
- **Bad,** because user still needs to know the process (gates, criteria)
- **Bad,** because no guided workflow for decision making

#### Scripts + AI skill — chosen

- **Good,** because scripts handle mechanical work (file creation, status, index)
- **Good,** because AI skill guides through the full protocol (explore → gates → finalize)
- **Good,** because AI assists with evidence research and quality checks
- **Bad,** because two-layer system: must maintain both scripts and skill

### File naming

#### No convention (freeform filenames)

- **Good,** because maximum flexibility
- **Bad,** because no ordering — hard to find the latest ADR
- **Bad,** because no way to reference ADRs by number

#### Zero-padded digits (`001-adr-<slug>.md`)

- **Good,** because visually aligned in directory listings
- **Bad,** because breaks at 1000 (lexical sort fails)
- **Bad,** because `-adr-` is redundant (directory is already `history/adr/`)

#### Plain digits without `-adr-` (`<NNN>-<slug>.md`) — chosen

- **Good,** because scales infinitely (no padding limit)
- **Good,** because shorter filenames
- **Good,** because `-adr-` infix is unnecessary (context from directory)
- **Bad,** because files aren't zero-padded — directory listing isn't visually aligned

## More Information

- This is the root ADR — nothing supersedes it
- [Michael Nygard — Documenting Architecture Decisions](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions)
- [MADR — Markdown Any Decision Records](https://adr.github.io/madr/)
- [ADR GitHub organization — templates and examples](https://adr.github.io/)
- [Architectural Significance Criteria (ASR Test) — Zimmermann](https://www.ozimmer.ch/practices/2020/09/24/ASRTestECSADecisions.html)
- [Definition of Done for ADs (ecADR) — Zimmermann](https://www.ozimmer.ch/practices/2020/05/22/ADDefinitionOfDone.html)
- [How to create ADRs — and how not to — Zimmermann](https://www.ozimmer.ch/practices/2023/04/03/ADRCreation.html)
- [Definition of Ready for ADs (START) — Zimmermann](https://ozimmer.ch/practices/2023/12/01/ADDefinitionOfReady.html)
- [The MADR Template Explained — Zimmermann](https://www.ozimmer.ch/practices/2022/11/22/MADRTemplatePrimer.html)
- [Decision-making ADRs: weightings are a work-around — Jacqui Read](https://jacquiread.com/posts/2024-09-11-decision-making-adrs-weightings-are-a-workaround/)
- [ADR Practices — adr.github.io](https://adr.github.io/ad-practices/)
- [Seven Architectural Decision Making Fallacies — Zimmermann](https://ozimmer.ch/practices/2025/09/01/ADMFallacies.html)

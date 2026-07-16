# Architecture Decision Records

This directory contains Architecture Decision Records (ADRs) for this project.

ADRs capture architecturally-significant decisions: why a particular approach was chosen, what alternatives were considered, and what the consequences are.

## Lifecycle

```
proposed → accepted → implemented → deprecated
```

| Status | Meaning |
|--------|---------|
| `proposed` | Under discussion, not yet finalized. ADR exists but may change. |
| `accepted` | Decision finalized, committed, ready to implement. |
| `implemented` | Code matching the decision is merged. |
| `deprecated` | Superseded by a newer ADR — see More Information section. |

## When is a decision worth an ADR? — ASR test

An ADR is worth creating when the decision meets **≥3** of these criteria:

| # | Criterion | Ask yourself... |
|---|-----------|-----------------|
| 1 | **External dependency** | Does this introduce a new library, framework, service, or platform API? |
| 2 | **Cross-cutting** | Does it affect multiple modules, targets (Android/iOS/JVM), or architectural layers? |
| 3 | **First-of-a-kind** | Haven't we made this type of decision in this project before? |
| 4 | **QoS impact** | Does it affect performance, security, offline capability, or UX quality? |
| 5 | **Past trouble** | Has this caused issues on a previous project (any project, not just this one)? |

See the [create-adr](../../.opencode/skills/create-adr/SKILL.md) skill for how this check is applied in practice.

## When is a decision ready to be made? — START

Before finalizing a decision, check readiness:

| # | Criterion | Check |
|---|-----------|-------|
| **S** | **Stakeholders** | Are decision makers and affected parties known? |
| **T** | **Time** | Has the most responsible moment arrived (not too early, not too late)? |
| **A** | **Alternatives** | Are ≥2 realistic options identified and understood? |
| **R** | **Requirements** | Is the problem, context, and decision criteria clear? |
| **T** | **Template** | Is the ADR format ready? (template.md exists — always yes ✅) |

(Adapted from [Zimmermann — Definition of Ready for ADs](https://ozimmer.ch/practices/2023/12/01/ADDefinitionOfReady.html))

## Template

See [template.md](template.md) for the canonical ADR template. New ADRs are created from this file by `./tools/adr/new.sh`.

Key sections:
- **Context and Problem Statement** — what problem, why now
- **Decision Drivers** — forces influencing the choice
- **Considered Options** — realistic alternatives
- **Decision Outcome** — chosen option + justification. Optionally add **Confidence** (High/Medium/Low) if uncertain.
- **Positive/Negative Consequences** — trade-offs
- **Pros and Cons of the Options** — per-option evaluation with **criteria at the same abstraction level** (see writing tip below)
- **More Information** — links to related ADRs, external resources, follow-up notes

> **Writing tip — compare at the same level:** When evaluating options, keep criteria at the same abstraction level. Don't compare "monthly cost" (concrete) with "ecosystem fit" (abstract). Split high-level criteria into ≤3-4 sub-criteria:
> ```
> ❌ Ecosystem fit: good
> ✅ API stability: good, Release cadence: moderate, Community size: large
> ```
> See [Jacqui Read — Decision-making ADRs](https://jacquiread.com/posts/2024-09-11-decision-making-adrs-weightings-are-a-workaround/)

## Quality checklist

Before finalizing an ADR from proposed → accepted, run this checklist:

| # | Check | Why |
|---|-------|-----|
| 1 | **Context clear?** — problem and drivers understandable without prior knowledge | Prevents blind flight (missing context) |
| 2 | **≥2 genuine alternatives?** — each is realistic, not a strawman | Prevents dummy alternative / rubber stamp |
| 3 | **Both pros and cons?** — positive AND negative consequences listed | Prevents fairy tale / free lunch coupon |
| 4 | **Requirements-based justification?** — references requirements or evidence, not trends | Prevents crowd-following, anecdotal evidence |
| 5 | **All sections filled?** — no `[placeholder]` or `...` remaining | Prevents unfinished ADRs |
| 6 | **References provided?** — links to related ADRs, docs, or external resources | Prevents orphan ADRs |

Sources: [Zimmermann — ADR anti-patterns](https://www.ozimmer.ch/practices/2023/04/03/ADRCreation.html),
[Zimmermann — Seven Fallacies](https://ozimmer.ch/practices/2025/09/01/ADMFallacies.html)

## Workflow

1. **Explore** — if the topic needs exploration, run a brainstorm session first (`/brainstorm`).
2. **Create** — `./tools/adr/new.sh "Decision title"` creates a new ADR file as **proposed**.
3. **Fill** — complete all sections in the generated file. Run the [quality checklist](#quality-checklist) before proceeding.
4. **Evidence** — ensure there is evidence the chosen option will work (research, past experience, or POC).
5. **Finalize** — once confident: `./tools/adr/status.sh <NNN> accepted`.
6. **Commit** — `docs(adr): add ADR for <decision>`. Commit **after** finalizing to accepted.
7. **Implement** — code in separate commits.
8. **Mark implemented** — `./tools/adr/status.sh <NNN> implemented`.
9. **Deprecate** — if superseded: `./tools/adr/status.sh <NNN> deprecated --superseded-by <MMM>`.

Commit formats:

| Action | Format |
|--------|--------|
| New ADR | `docs(adr): add ADR for <title>` |
| Status update | `docs(adr): mark <NNN> as <status>` |
| Deprecation | `docs(adr): mark <NNN> as deprecated by <MMM>` |

## File naming

ADRs are stored as `history/adr/<NNN>-<slug>.md` where NNN is a sequential
positive integer starting at 1. Example: `1-initialize-adr-process.md`.

## Deferred extensions

Ideas for future expansion, not implemented yet:

| # | Extension | Effort | Why deferred |
|---|-----------|--------|--------------|
| E4 | Decision graph generator — parse Links/More Information and produce a Mermaid dependency graph | M | Needs ≥3 ADRs to be useful |
| E5 | ADR linting in CI — validate required sections, status transitions, and formatting | M | Add after 3-4 ADRs once pattern stabilizes |
| E7 | `@adr` code tags — structured comments in source files linking back to governing ADRs | M | Adds maintenance burden now; more valuable with more ADRs |

## Index

See [INDEX.md](INDEX.md) for the complete list of ADRs.

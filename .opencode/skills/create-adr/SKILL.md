---
name: create-adr
description: Create Architecture Decision Records following the project's ADR process.
---

# Create ADR

Create an Architecture Decision Record for an architecturally-significant decision.

## Activation

User says: "create ADR", "document this decision", "capture this decision",
or similar. **Do NOT auto-create** — wait for explicit user approval.

## Protocol

### 1. Explore the topic

Start by asking about the decision:

- What is the problem or context?
- What options were considered?
- What was chosen and why?
- What are the trade-offs / consequences?
- What does this decision affect (dependencies)?

If the user already completed a brainstorm session on this topic, reference
the artifact in `history/brainstorms/` to fill the ADR sections.

### 2. Check significance — ASR test

Before creating an ADR, evaluate whether this decision warrants one.
Use the ASR test from `history/adr/README.md`:

```
[ ] External dependency — new library, framework, service, or platform API?
[ ] Cross-cutting — affects multiple modules, targets, or layers?
[ ] First-of-a-kind — haven't made this type of decision in this project before?
[ ] QoS impact — affects performance, security, offline, or UX quality?
[ ] Past trouble — caused issues on a previous project?
```

If **0-2** criteria are met:
→ "This seems tactical. An ADR is probably overkill. Proceed anyway?"

If **≥3** criteria are met:
→ Proceed — this decision is architecturally significant.

### 3. Check readiness — START

Use the START criteria from `history/adr/README.md`:

```
[ ] S — Stakeholders: are affected parties known?
[ ] T — Time: is this the most responsible moment?
[ ] A — Alternatives: are ≥2 realistic options identified?
[ ] R — Requirements: is the problem and criteria clear?
[ ] T — Template: our ADR format is ready ✅
```

If **≥3** fail → "This decision doesn't seem ready yet. What's missing?"
If **0-2** fail → proceed.

### 4. Wait for approval

Present a summary of the proposed ADR and ask:
"Shall I create the ADR?" **Do NOT create without explicit approval.**

### 5. Run the script

On approval:

```bash
./tools/adr/new.sh "Short decision title"
```

This creates a file at `history/adr/<NNN>-<slug>.md` with Status=**proposed**
and Date=today. The ADR is not finalized yet — it can still change.

### 6. Fill the template

Write all sections using the gathered context:

- **Context and Problem Statement** — what problem, why now
- **Decision Drivers** — forces that influenced the choice
- **Considered Options** — realistic alternatives. Keep criteria at the same abstraction level (see README tip).
- **Decision Outcome** — chosen option + justification. Optionally add **Confidence** level if uncertain.
- **Positive/Negative Consequences** — trade-offs
- **Pros and Cons per option** — detailed per-option reasoning
- **More Information** — links to related ADRs, external resources, follow-up notes

### 7. Gather evidence

Before finalizing from proposed → accepted, check:

**Is there evidence this option will work?**

Evidence tiers:
| Risk level | Sufficient evidence |
|-----------|-------------------|
| Low (easy to undo) | Research — docs, articles, community examples |
| Medium | Past experience in similar context, or targeted research |
| High (costly to undo) | Proof-of-concept, spike, or prototype |

**If evidence exists:** document it in Decision Outcome or More Information.

**If no evidence and research is feasible:**
→ Launch a background research task to investigate options and return a structured
evidence summary with findings and a recommendation.

**If the research task fails** (tool error, timeout, etc.):
→ Give the user this prompt to run in a fresh session:

```
I need research on: <decision title>

Context: <brief from the filled ADR>

Options considered:
  - <option A>
  - <option B>

Please investigate:
  - Official documentation for each option
  - Real-world KMP usage examples on GitHub
  - Known limitations or risks
  - Community maturity and support

Return a structured evidence report with findings and a recommendation.
```

**If no evidence and user declines research:**
→ "The ADR stays as proposed. Run `./tools/adr/status.sh <NNN> accepted` when ready."

### 8. Quality check

Before finalizing, run through the quality checklist from `history/adr/README.md`:

| # | Check |
|---|-------|
| 1 | Context clear? |
| 2 | ≥2 genuine alternatives? |
| 3 | Both pros and cons? |
| 4 | Requirements-based justification? |
| 5 | All sections filled? |
| 6 | References provided? |

If any check fails → "Item <N> needs attention. Fix it, or proceed anyway?"
If all pass → proceed to finalize.

### 9. Finalize

Once evidence is in place and quality check passes:

```bash
./tools/adr/status.sh <NNN> accepted
```

Status moves from proposed → accepted. The decision is now final.

### 10. Notify to commit

After finalizing, tell the user to commit:

```bash
git add history/adr/<NNN>-<slug>.md
git commit -m "docs(adr): add ADR for <title>"
```

Commit **before** any code changes for this decision.

## Template

See `history/adr/template.md` for the canonical template.

## Guidelines

- One ADR per architectural decision
- Concrete over abstract — name specific libraries, versions, patterns
- Status lifecycle: proposed → accepted → implemented → deprecated
- Include external references in More Information (GitHub issues, docs, articles)
- Disclose confidence level if uncertain
- Keep option criteria at the same abstraction level
- Watch for common decision fallacies (blind flight, crowd-following, golden hammer) — see [Zimmermann's Seven Fallacies](https://ozimmer.ch/practices/2025/09/01/ADMFallacies.html)
- Commit AFTER finalizing to accepted, BEFORE writing code

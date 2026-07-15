---
type: ai
---

# Proactive Quality Review

Mandatory quality gate before marking ANY artifact complete. Examples use OpenSpec
artifact names but the same dimensions apply universally.
When artifacts don't match OpenSpec naming, map before reviewing:

| OpenSpec name | Generic equivalent | Examples |
|---|---|---|
| proposal.md | Scoping doc | PRD, RFC, one-pager, design brief |
| specs/ | Requirements | User stories, acceptance criteria, BDD scenarios |
| design.md | Architecture doc | Tech spec, system design, ADR |
| impl_details.md | Implementation plan | Code-level plan, developer guide, module spec |
| tasks.md | Task list | Sprint backlog, checklist, work breakdown |

If reviewing a single doc (not a full artifact set), apply all 7 concerns to that
document's content — the per-artifact prompts are suggestions, not requirements.

---

## Tier 1: Quick Rejection (30s — fail on ANY)

Scan for deal-breakers. If found → fix immediately, don't proceed to deep review.

- **Placeholders**: `TODO`, `FIXME`, `~`, `TBD`, `<id>`, `<path>`, "..." at sentence end.
  Also: "approximately", "roughly", "around", "ballpark" when specifying concrete numbers
  or sizes — replace with exact count or remove the claim.
- **Empty sections**: Header with no body; `N/A` without explanation of WHY not applicable
- **Phantom references**: Class/method/file name that doesn't exist in codebase (grep verify)
- **Vague scope**: "improve", "enhance", "make better" without concrete deliverables
- **Vague actions**: Verb without object — "fix it", "update the file", "add tests",
  "handle errors". What specifically? Where? How verified?
- **Unresolved decisions**: "either X or Y", "we could..." without conclusion
- **Missing acceptance**: Task has no observable way to know when it's DONE

---

## Tier 2: Deep Review — Seven Concerns

For each concern, check every artifact in the set. If gap → fix. If N/A → state why
explicitly (e.g., "Security: no user data, no network in this change").

### 1. Ambiguities — would 3 different engineers interpret this the same way?

**Scoping doc** (proposal): Capabilities → concrete deliverables? Non-goals → explicit boundaries?
**Requirements** (specs): Every scenario → observable WHEN/THEN? Exact counts, not "several"?
  Error/empty/loading states covered? Not just happy-path?
**Architecture** (design): Decisions → WHY + rejected alternatives + tradeoffs?
  Open Questions → every one has a corresponding task?
**Implementation plan** (impl_details): Signatures → real classes, real packages?
  Hazards → runtime traps (NPE, leak, race), not behavioral corner-case repeats?
**Task list** (tasks): Each checkbox → specific deliverable? "Update" → WHICH file? "Add" → WHERE?

**Red-flag phrases**: "as needed", "when ready", "later", "figure out", "should", "normally"

### 2. Missing Context — can a fresh AI session implement without asking?

**Litmus test**: Would a new developer reading only these artifacts produce correct
implementation with zero clarifying questions? One question = one missing piece of
context → find it and add it. If any question remains → not ready.

**Concrete minimums across all artifacts**:
- File paths: `src/<sourceSet>/kotlin/<package>/<File>.kt` — never "in the module"
- Dependencies: lib name + version, module name — never "the usual ones"
- Execution order: which file creates a dependency others need? Is creation order stated?
- Patterns: reference existing code file+line — never "follow the established pattern"
- State: initial state + final state explicit — never "it should be set up properly"

### 3. Corner Cases — what breaks?

**For EACH task in any task list**, exhaust at minimum:

| Category | Check |
|---|---|
| Input | empty, null, max-length, unicode, special chars, negative, zero, NaN |
| State | first-run, after-kill, after-crash, offline, low-storage, permission-denied |
| Timing | concurrent writes, rapid-toggle, slow-network, timeout, background-thread |
| Platform | config-change, process-death, low-memory, Doze, SDK-version-differences |

**Requirements** (specs): Error scenarios outnumber happy-path? If not → gap.
**Implementation plan** (impl_details): Hazards table → runtime traps, not behavioral repeats.

### 4. Testing — how do we KNOW this works?

**Task list** (tasks): Per-task verification method stated: unit / device / manual / none-with-reason
**Requirements** (specs): Smoke scenarios → concrete test class traceable?
**Implementation plan** (impl_details): Testability Plan → layers vs test types filled? Skipped explained?

**Anti-pattern**: "We'll write tests later" or "QA will catch it" = gap.

### 5. Security — what could be exploited or leaked?

Does this change involve ANY of:
- User data / PII → where stored? encrypted at rest?
- File I/O → app-private or external storage? path traversal possible?
- Network → HTTPS enforced? sensitive data in URL params or headers?
- Permissions → new `<uses-permission>`? runtime check before use?
- Auth → token storage mechanism? refresh flow? expiry handling?
- Logging → secrets or PII in logcat/crash reports?
- Screenshots → file path? visible to gallery? shared via intent?

### 6. Performance — what's slow?

**All artifacts**: Main-thread DB queries? unbounded lists? bitmap loading without sizing?
  nested loops over unknown N? JSON parsing 10k+ items? broadcast on main thread?
**Implementation plan** (impl_details): Hot-path concerns identified? Prevention stated?
**Task list** (tasks): Heavy ops in tasks flagged? Dispatcher strategy mentioned?

### 7. Cross-Artifact Consistency

- Every task ← traces to → a spec scenario (can't implement what's not specified)
- Every spec scenario ← traces to → a task (can't verify what's not implemented)
- Architecture decisions → reflected in implementation plan (design says "Room" → plan shows Room)
- Every Open Question in design → corresponding task (no orphaned questions)
- Numbers match: spec declares N scenarios → exactly N tasks covering them
- Names consistent: same class called "UserRepo" in design, not "UserRepository" in tasks

**Before consistency checks**, map all artifact types to their equivalents (see intro table).
If artifacts use different naming conventions (e.g., "PRD" vs "proposal"), standardize
mentally before checking traceability.

---

## Tier 3: Implementation Readiness

Final gate before declaring artifacts "ready":

1. All Tier 1 checks pass (no placeholders, no vague, no phantom refs)
2. All 7 concerns addressed per artifact (fixed or N/A-with-reason)
3. Cross-artifact traceability verified (spec ↔ task, design ↔ plan)
4. Fresh session litmus test: would a new AI agent reading only these artifacts need
   ZERO questions to implement all tasks? Count as a question: any ambiguity that forces
   guessing a file path, class name, module name, dependency version, API signature,
   or decision not stated in artifacts. If ≥1 question → not ready.

---

## Usage Rules

1. **Tier 1 first** — reject on placeholders/vague/empty before deep review. 30s max.
2. **Tier 2 second** — all 7 concerns, all artifacts. Fix gaps immediately.
3. **Tier 3 last** — cross-check + readiness gate. Final yes/no.
4. **Fix, don't note** — never "the implementer will figure this out later"
5. **Re-scan after fixes** — any change → re-run Tier 1
6. **Skip only with reason** — "N/A: no user data" is valid. Silent skip is not.
   If ≥3 concerns marked N/A, re-examine: are you reviewing deeply enough or just
   trying to finish? Each N/A justification must be independently defensible.
7. **Self-improvement**: If any checklist rule misses something, misleads, is too
   broad/narrow, or fails to catch a real quality issue you observed — edit
   `proactive-review.md` to improve it before continuing. This document governs its
   own evolution. Don't note the gap and move on; apply the fix.

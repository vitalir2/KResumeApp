# Git Guidelines

This document describes the Git conventions used in the project.

## Commit Message Format

All commit messages follow the Conventional Commits format:

```
type(scope?): subject
```

- **Subject**: ≤70 characters, imperative mood (e.g., "Add login screen" not "Added login screen")
- **Scope**: Optional, but encouraged for larger areas (e.g., `feat(auth):`, `fix(ui):`)

### Allowed commit types

| Type | When to Use |
|------|------------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `chore` | Maintenance, configuration, dependencies |
| `docs` | Documentation changes |
| `refactor` | Code restructuring without behavior change |
| `revert` | Reverting a previous commit |

### What the commit hook rejects

- Unknown commit types (not in the allowed list)
- Missing colon after type/scope
- Empty subject
- `WIP`, `fixup!`, `squash!` messages

### What the commit hook allows

- `Revert "..."` messages
- `Merge branch '...'` messages

## --no-verify is PROHIBITED

AI agents and human developers **MUST NOT** use `git commit --no-verify` to bypass commit-msg or pre-commit hooks.

- If the **commit-msg hook** rejects a message, fix the message format to comply
- If the **pre-commit hook** (ktlint) fails, inspect and fix the code violations

Bypassing hooks undermines code quality enforcement. Always fix the underlying issue instead.

## Commit Preparation

Before committing:

1. **Check staged files**: Run `git diff --cached --name-only` to verify only intended files are staged
2. **Verify subject length**: Ensure the subject is ≤70 characters

## Git Stash Discipline

- **Never** use `git stash` without explicit paths — `git stash` stashes ALL changes (staged + unstaged), which can lose context. Instead, use `git stash push -m "reason" -- <paths>` to stash only specific files.
- Prefer using project scripts over raw Git commands when they are available.

## Branch Strategy

**Trunk-based development**: All work is done on `main`. No long-lived branches.

## Pre-commit Hooks

The pre-commit hook runs `tools/formatter/pre-commit` (ktlint) on staged `.kt` files. It must pass before committing. Fix any reported violations — do not bypass.

## Merge Discipline

- **Rebase-only**: Use `git pull --rebase` to integrate remote changes
- **Linear history**: No merge commits — all integration is via rebase
- **Squash or rebase** instead of merging branches

## Secrets

Do not commit secrets (API keys, passwords, certificates, etc.). The `.gitignore` includes patterns like `*secret*`, `*credential*`, and `*.jks`, but this is not a security boundary. If a secret is accidentally committed, rotate it immediately.

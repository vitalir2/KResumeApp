---
type: both
synced_at: sha256:807d7cbd323d36c661d7c61eed1b72ef323928c230e02014e51b90027a5e153e
---
# Git Guidelines

## Commit Format

`type(scope?): subject` — subject ≤70 chars, imperative mood.

Allowed types: `feat`, `fix`, `chore`, `docs`, `refactor`, `revert`.

Hook rejects: unknown types, missing colon, empty subject, `WIP`, `fixup!`, `squash!`.
Hook allows: `Revert "..."`, `Merge branch '...'`.

## --no-verify PROHIBITED

AI agents MUST NOT use `git commit --no-verify` to bypass commit-msg validation.
If the hook rejects a message, fix the message format, do not bypass.
If the pre-commit hook (ktlint) fails, inspect and fix the violations — do not bypass.

## Commit Preparation

Before `git add`, check what's staged: `git diff --cached --name-only`.
Before `git commit`, check subject length: ≤70 chars.

## Git stash discipline

- **Never `git stash` without explicit paths** — `git stash` stashes ALL changes (staged + unstaged), which can lose context. Use `git stash push -m "reason" -- <paths>`.
- Prefer scripts over raw git when available.

## Branch Strategy

Trunk-based. All work on `main`. No long-lived branches.

## Pre-commit

Runs `tools/formatter/pre-commit` (ktlint). Must pass. Fix violations — do not bypass.

## Merge Discipline

Rebase-only. `git pull --rebase`. Linear history. No merge commits.

## Secrets

Do not commit secrets. If accidentally committed, rotate immediately.

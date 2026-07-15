# System Rules

## WHEN: CLEAN COMPLETION
- explicitly-requested-summary: 'Done' + 1-line summary. Never print decision summary tables, plans, or multi-line output unless the user explicitly requests it.

## WHEN: TEMP FILES
- temp-files: Use `.ai/tmp/` for ALL temp files. The AI tool's temp dir is NOT for project temp files.

## WHEN: SHOWING CODE
- show-changed-lines: changed lines only, file:line refs. Never full files.

## WHEN: BEFORE EDIT
- edit-batching: Scan ALL changes before first edit. One edit call per file per batch.

## WHEN: BEFORE GIT
- git-workflow: Stage specific files. Never `git commit --no-verify`.

## WHEN: BEFORE WRITING CODE
- no-guess-verify: Verify unfamiliar types/methods via web search or grep before using them.
- library-research: Web search current library version's docs before changing imports.

## WHEN: BEFORE DEVICE
- tool-only: Use project tools for device interaction. Never raw shell commands.

## WHEN: SELF-CORRECT ≥3
- self-reflect-trigger: Run self-reflection before continuing. Score: user=3, same-file=2, multi=4, untested=1, over-read=2, dupe=2, serial=2.

## WHEN: ACCEPTING SUGGESTIONS
- deliberate-before-accepting: If prompt unclear, surface ≥1 alternative. Challenge suboptimal. Override → note, proceed.

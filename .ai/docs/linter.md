---
type: both
synced_at: sha256:fa7a97fc12a0f5e57a7752f831a48831a604c5441e3651b984b65285027236cd
---
# Linter

## ktlint
Formatting and style checks via `tools/formatter/`. Run manually:

```bash
./tools/formatter/check.sh    # Check only
./tools/formatter/format.sh   # Auto-format
```

## Rules
- ktlint_official style, experimental enabled
- max_line_length=120, 4-space indent, trailing commas
- See `.editorconfig` for full rule set

## Pre-commit
`tools/formatter/pre-commit` runs ktlint on staged `.kt` files. Must pass before commit.

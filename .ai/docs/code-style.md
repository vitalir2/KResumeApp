---
type: both
synced_at: sha256:6bfa2fd2c16eeb2402efcd04cbd0f9e0647644993dab958ccd3d89d5228c5980
---
# Code Style

## Formatting
- indent: space, 4; max_line_length: 120; eol: lf; final_newline: yes; trim_trailing_whitespace: yes
- ktlint: ktlint_official style, experimental enabled
- trailing commas: allowed on decls + call sites
- disabled ktlint rules: function-naming, backing-property-naming, class-naming, filename, no-empty-file, no-wildcard-imports
- ktlint disabled for: generated/, *.bat, **/build/**

## Imports
- assertions: `io.kotest.matchers.*` only
- testing annotations: `kotlin.test.Test`, `kotlin.test.BeforeTest`, `kotlin.test.AfterTest`, `kotlin.test.Ignore`
- mock libs: forbidden (MockK, Mockito, EasyMock, PowerMock)

## Naming
- simple functions after `@Composable`; batch all imports

## Structural
- functions < 150 lines; no default args unless requested
- no `;`; branch braces always (never single-line)
- KDoc `/** */` only — never `//` comment style for KDoc; file-level uses `//`, API uses `/** */`
- pre-commit: `./tools/formatter/format.sh`

## Replacement Table
| Old | New |
|-----|-----|
| org.junit.Test | kotlin.test.Test |
| org.junit.Before | kotlin.test.BeforeTest |
| org.junit.After | kotlin.test.AfterTest |
| org.junit.Ignore | kotlin.test.Ignore |
| org.junit.Assert.* | io.kotest.matchers.* |
| org.hamcrest.* | io.kotest.matchers.* |
| kotlin.test.assert* | io.kotest.matchers.* |

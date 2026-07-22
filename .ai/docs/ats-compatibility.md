---
type: ai
---

# ATS Compatibility

How we verify resumes parse in ATS systems.

## Golden Rules

| Rule | Rationale |
|------|-----------|
| Semantic HTML: `h1` name, `h2` section, `h3` title, `ul/li` bullets | ATS parsers anchor on these tags |
| Position ≠ Company in separate elements | Combined = parser misses one |
| Contact in visible body text | Not in meta/alt/CSS-generated content |
| No CSS hiding/reordering | `display:none`, `abs pos`, `order` break raw DOM |
| No images for text | Parser can't read them |
| Min font 10pt | Smaller gets ignored |
| Standard date format `YYYY-MM` | Ordinals/relatives confuse parsers |

## Checks (automated in PoC renderer)

- `h1` contains name
- `h3` contains position; company in separate `<div>`
- Email, phone, location in plain text
- Dates use `-` not em dash
- No `display:none` or `visibility:hidden` in inline CSS
- Lists use `<ul><li>`

## Manual Test

1. Ctrl+A the HTML preview → paste into Notepad
2. Text reads in order: name → contact → summary → experience
3. No garbled chars, no HTML tags visible, dates clear

## Real App Feature

`GET /api/ats-score` — returns `{ score, checks[], recommendations[] }`
so users can validate before exporting.

See `docs/ats-compatibility.md` for full guide + tooling references.

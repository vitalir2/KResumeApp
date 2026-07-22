---
type: human
---

# ATS Compatibility Guide

How to verify that generated resumes parse correctly in Applicant Tracking Systems.

## Why ATS Compatibility Matters

ATS parsers extract structured data from resumes (name, contact, work history, skills)
by reading the raw HTML. Unlike browsers, they don't execute CSS or JavaScript.
If content is hidden in complex markup, the parser skips it.

## The Golden Rules

| Rule | Why |
|------|-----|
| **Semantic HTML first** | `<h1>` for name, `<h2>` for sections, `<h3>` for job titles, `<ul><li>` for lists. ATS parsers anchor on these. |
| **Position ≠ Company** | Never combine job title and employer in the same element. `<h3>Title</h3>` + `<div>Company</div>` — separate. |
| **Contact info in visible text** | Email, phone, location must be plain text in the body, not inside `<meta>`, `alt`, or CSS-generated content. |
| **No CSS hiding/reordering** | Never use `display:none`, `visibility:hidden`, `position:absolute`+offsets, `order`, or `transform` to rearrange content. Some parsers ignore CSS entirely; the raw DOM order is what matters. |
| **No images for text** | Logos, icons, or stylized headers with text-in-image go unread. If you must use images, provide `alt` text. |
| **Minimum font size 10pt** | Tiny text is either ignored or flagged as suspicious. |
| **Standard date format** | Prefer `YYYY-MM` or `MMM YYYY`. Avoid ordinal suffixes (Jan 1st, 2024) or relative dates ("present"). |

## Automated Checks (run on every render)

These are the checks the PoC passes currently. They can be automated as a test suite:

```kotlin
data class AtsCheck(val name: String, val pass: Boolean)

fun checkAtsCompatibility(html: String): List<AtsCheck> {
    return listOf(
        AtsCheck("Name in <h1>",        html.contains("<h1>")),
        AtsCheck("Position in <h3>",    html.contains("<h3>")),
        AtsCheck("Lists use <ul>/<li>", html.contains("<ul>") && html.contains("<li>")),
        AtsCheck("No em dash",          !html.contains("\u2014")),
        AtsCheck("No display:none",     !html.contains("display:\\s*none".toRegex())),
        AtsCheck("No visibility:hidden",!html.contains("visibility:\\s*hidden".toRegex())),
        AtsCheck("Email visible",       containsContactInfo(html, "email")),
        AtsCheck("Phone visible",       containsContactInfo(html, "phone")),
    )
}
```

## Manual Testing Tools

| Tool | What it checks | Free? |
|------|---------------|-------|
| [Jobscan](https://www.jobscan.co/) | ATS match rate against job descriptions | Limited free |
| [TopResume ATS Test](https://www.topresume.com/resume-review) | Free resume review with ATS feedback | Yes |
| [CVCompiler](https://cvcompiler.com/) | ATS readability score | Limited free |
| **Saving as .txt** | If you "Save As… TXT" and the text reads in correct order, most ATS will parse it correctly | Free |

### Quick Manual Test

1. Render the resume HTML
2. Open in browser → Ctrl+A → Ctrl+C
3. Paste into a plain text editor (TextEdit, Notepad)
4. Verify:
   - Name appears first
   - Contact info follows
   - Sections are in logical order
   - No garbled text, stray HTML tags, or unicode issues
   - Dates are readable

## Real-App Feature: ATS Score Endpoint

For the production app, we should add an endpoint that analyzes
the rendered resume and returns an ATS compatibility score:

```
GET /api/ats-score?dsl=<encoded DSL>
→ {
    "score": 87,
    "checks": [
      {"name": "Name in <h1>", "pass": true},
      {"name": "Position vs Company", "pass": true},
      {"name": "Contact visible", "pass": true},
      ...
    ],
    "recommendations": [
      "Add a skills section for better keyword matching",
      "Avoid using tables for layout"
    ]
  }
```

This would let users preview how ATS-friendly their resume is
before exporting.

## References

- [Google: ATS-Friendly Resume Guidelines](https://www.google.com/search?q=ats+friendly+resume+guidelines)
- [Kotlin HTML parsing libraries](https://github.com/JetBrains/kotlinx.html) for implementing automated checks

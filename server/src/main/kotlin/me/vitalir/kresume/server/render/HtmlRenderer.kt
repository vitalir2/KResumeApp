package me.vitalir.kresume.server.render

import me.vitalir.kresume.server.model.Resume

object HtmlRenderer {

    fun render(resume: Resume): String {
        return buildString {
            appendLine("<!DOCTYPE html>")
            appendLine("<html lang=\"en\">")
            appendLine("<head>")
            appendLine("<meta charset=\"UTF-8\">")
            appendLine("<title>${escapeHtml(resume.basics?.name ?: "Resume")}</title>")
            appendLine("<style>")
            appendLine(renderCss())
            appendLine("</style>")
            appendLine("</head>")
            appendLine("<body>")
            renderHeader(resume)
            renderSummary(resume)
            renderWork(resume)
            appendLine("</body>")
            appendLine("</html>")
        }
    }

    private fun renderCss(): String = """
        body {
            font-family: "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            font-size: 11pt;
            line-height: 1.45;
            color: #111;
            max-width: 8.5in;
            margin: 0.5in auto;
            padding: 0 0.5in;
            background: #fff;
        }
        h1 {
            font-size: 1.8em;
            font-weight: 700;
            margin: 0 0 4px 0;
            color: #000;
        }
        h1 + div {
            font-size: 1.05em;
            color: #444;
            margin-bottom: 10px;
        }
        .contact {
            font-size: 0.95em;
            color: #333;
            margin-bottom: 22px;
            padding-bottom: 10px;
            border-bottom: 1px solid #bbb;
        }
        .contact span {
            margin-right: 18px;
            white-space: nowrap;
        }
        .contact span:not(:last-child)::after {
            content: "·";
            margin-left: 18px;
            color: #888;
        }
        h2 {
            font-size: 1.15em;
            font-weight: 700;
            color: #000;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            border-bottom: 1px solid #333;
            padding-bottom: 3px;
            margin: 22px 0 10px 0;
        }
        .summary p {
            margin: 0 0 10px 0;
        }
        .job {
            margin-bottom: 18px;
            page-break-inside: avoid;
        }
        .job h3 {
            font-size: 1.05em;
            font-weight: 700;
            margin: 0 0 2px 0;
            color: #000;
        }
        .job .company {
            font-size: 1em;
            font-weight: 600;
            color: #222;
            margin-bottom: 2px;
        }
        .job .dates {
            font-size: 0.9em;
            color: #555;
            margin-bottom: 6px;
            text-align: right;
        }
        .job .desc {
            margin-bottom: 6px;
        }
        ul {
            list-style: disc;
            padding-left: 20px;
            margin: 0 0 6px 0;
        }
        li {
            margin-bottom: 2px;
        }
    """.trimIndent()

    private fun StringBuilder.renderHeader(resume: Resume) {
        val basics = resume.basics ?: return
        val name = basics.name ?: return
        appendLine("<h1>${escapeHtml(name)}</h1>")
        basics.title?.let { title ->
            appendLine("<div>${escapeHtml(title)}</div>")
        }
        val contact = buildList {
            basics.email?.let { add(it) }
            basics.phone?.let { add(it) }
            basics.location?.let { add(it) }
            basics.url?.let { add(it) }
        }
        if (contact.isNotEmpty()) {
            appendLine("<div class=\"contact\">")
            contact.forEach { append("<span>${escapeHtml(it)}</span>") }
            appendLine("</div>")
        }
    }

    private fun StringBuilder.renderSummary(resume: Resume) {
        val summary = resume.basics?.summary ?: return
        appendLine("<div class=\"section summary\">")
        appendLine("<h2>Summary</h2>")
        appendLine("<p>${escapeHtml(summary)}</p>")
        appendLine("</div>")
    }

    private fun StringBuilder.renderWork(resume: Resume) {
        val work = resume.work ?: return
        appendLine("<h2>Experience</h2>")
        for (entry in work) {
            appendLine("<div class=\"job\">")
            entry.position?.let { appendLine("<h3>${escapeHtml(it)}</h3>") }
            entry.company?.let { appendLine("<div class=\"company\">${escapeHtml(it)}</div>") }
            if (entry.startDate != null || entry.endDate != null) {
                val start = entry.startDate ?: ""
                val end = entry.endDate ?: ""
                appendLine("<div class=\"dates\">${escapeHtml(start)} - ${escapeHtml(end)}</div>")
            }
            entry.description?.let { desc ->
                appendLine("<div class=\"desc\">${escapeHtml(desc)}</div>")
            }
            entry.highlights?.let { highlights ->
                if (highlights.isNotEmpty()) {
                    appendLine("<ul>")
                    for (h in highlights) {
                        appendLine("<li>${escapeHtml(h)}</li>")
                    }
                    appendLine("</ul>")
                }
            }
            appendLine("</div>")
        }
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}

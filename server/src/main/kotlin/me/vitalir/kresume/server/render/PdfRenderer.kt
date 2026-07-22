package me.vitalir.kresume.server.render

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import me.vitalir.kresume.server.model.Resume
import java.io.ByteArrayOutputStream

object PdfRenderer {

    fun render(resume: Resume): ByteArray {
        val html = buildPdfHtml(resume)
        val os = ByteArrayOutputStream()
        PdfRendererBuilder()
            .withHtmlContent(html, null)
            .toStream(os)
            .run()
        return os.toByteArray()
    }

    private fun buildPdfHtml(resume: Resume): String = buildString {
        appendLine("""<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"/>""")
        appendLine("<style>")
        appendLine(pdfCss())
        appendLine("</style></head><body>")
        appendLine("<div class=\"container\">")
        renderHeader(resume)
        renderSummary(resume)
        renderWork(resume)
        appendLine("</div></body></html>")
    }

    private fun pdfCss(): String = """
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Helvetica', 'Arial', sans-serif; font-size: 11pt; color: #333; line-height: 1.5; }
        .container { max-width: 700px; margin: 0 auto; padding: 20pt; }
        .header { border-bottom: 2pt solid #333; margin-bottom: 12pt; padding-bottom: 8pt; }
        .header h1 { font-size: 18pt; margin-bottom: 2pt; }
        .header .title { font-size: 12pt; color: #666; }
        .section { margin-bottom: 14pt; }
        .section h2 { font-size: 13pt; border-bottom: 1pt solid #ccc; padding-bottom: 3pt; margin-bottom: 6pt; }
        .summary p { font-size: 10pt; color: #444; }
        table.work { width: 100%; border-collapse: collapse; }
        table.work td { vertical-align: top; padding: 4pt 0; }
        table.work td.company { font-weight: bold; width: 30%; padding-right: 8pt; }
        table.work td.details { width: 70%; }
        table.work td .position { font-weight: bold; font-size: 10pt; }
        table.work td .dates { font-size: 9pt; color: #888; }
        table.work td .desc { font-size: 10pt; margin-top: 2pt; }
        ul.highlights { list-style: disc; padding-left: 14pt; font-size: 10pt; margin-top: 2pt; }
    """.trimIndent()

    private fun StringBuilder.renderHeader(resume: Resume) {
        val b = resume.basics ?: return
        val name = b.name ?: return
        appendLine("<div class=\"header\">")
        appendLine("<h1>${escapeHtml(name)}</h1>")
        b.title?.let { appendLine("<div class=\"title\">${escapeHtml(it)}</div>") }
        appendLine("</div>")
    }

    private fun StringBuilder.renderSummary(resume: Resume) {
        val s = resume.basics?.summary ?: return
        appendLine("<div class=\"section summary\"><h2>Summary</h2><p>${escapeHtml(s)}</p></div>")
    }

    private fun StringBuilder.renderWork(resume: Resume) {
        val work = resume.work ?: return
        appendLine("<div class=\"section\"><h2>Work Experience</h2>")
        appendLine("<table class=\"work\">")
        for (entry in work) {
            appendLine("<tr><td class=\"company\">${escapeHtml(entry.company ?: "")}</td>")
            appendLine("<td class=\"details\">")
            if (entry.position != null) appendLine("<div class=\"position\">${escapeHtml(entry.position)}</div>")
            val start = entry.startDate ?: ""
            val end = entry.endDate ?: ""
            if (start.isNotEmpty() || end.isNotEmpty()) appendLine("<div class=\"dates\">${escapeHtml(start)} — ${escapeHtml(end)}</div>")
            entry.description?.let { appendLine("<div class=\"desc\">${escapeHtml(it)}</div>") }
            entry.highlights?.let { highlights ->
                if (highlights.isNotEmpty()) {
                    appendLine("<ul class=\"highlights\">")
                    highlights.forEach { appendLine("<li>${escapeHtml(it)}</li>") }
                    appendLine("</ul>")
                }
            }
            appendLine("</td></tr>")
        }
        appendLine("</table></div>")
    }

    private fun escapeHtml(text: String): String = text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
}

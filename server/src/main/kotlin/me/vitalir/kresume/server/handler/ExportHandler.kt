package me.vitalir.kresume.server.handler

import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respondBytes
import kotlinx.serialization.Serializable
import me.vitalir.kresume.server.compiler.CompileResult
import me.vitalir.kresume.server.compiler.ResumeCompiler
import me.vitalir.kresume.server.render.PdfRenderer

@Serializable
data class ExportRequest(val dsl: String)

suspend fun handleExportPdf(call: ApplicationCall, compiler: ResumeCompiler) {
    val request = call.receive<ExportRequest>()
    when (val result = compiler.compile(request.dsl)) {
        is CompileResult.Success -> {
            try {
                val pdf = PdfRenderer.render(result.resume)
                call.response.header("Content-Disposition", "attachment; filename=\"resume.pdf\"")
                call.respondBytes(pdf, ContentType.Application.Pdf)
            } catch (e: Exception) {
                call.respondBytes(
                    "PDF generation failed: ${e.message}".toByteArray(),
                    ContentType.Text.Plain
                )
            }
        }
        is CompileResult.Error -> {
            call.respondBytes(
                "Compilation failed: ${result.message}".toByteArray(),
                ContentType.Text.Plain
            )
        }
    }
}

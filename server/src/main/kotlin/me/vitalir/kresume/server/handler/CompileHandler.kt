package me.vitalir.kresume.server.handler

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable
import me.vitalir.kresume.server.compiler.CompileResult
import me.vitalir.kresume.server.compiler.ResumeCompiler
import me.vitalir.kresume.server.render.HtmlRenderer

@Serializable
data class CompileRequest(val dsl: String)

@Serializable
data class CompileResponse(
    val status: String,
    val html: String,
    val compileTimeMs: Double
)

suspend fun handleCompile(call: ApplicationCall, compiler: ResumeCompiler) {
    val request = call.receive<CompileRequest>()
    when (val result = compiler.compile(request.dsl)) {
        is CompileResult.Success -> {
            val html = HtmlRenderer.render(result.resume)
            call.respond(CompileResponse("ok", html, result.compileTimeMs))
        }
        is CompileResult.Error -> {
            call.respond(CompileResponse("error", result.message, 0.0))
        }
    }
}

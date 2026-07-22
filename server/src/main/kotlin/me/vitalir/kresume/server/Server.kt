package me.vitalir.kresume.server

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import me.vitalir.kresume.server.compiler.ResumeCompiler
import me.vitalir.kresume.server.handler.handleBench
import me.vitalir.kresume.server.handler.handleCompile
import me.vitalir.kresume.server.handler.handleExportPdf

private val indexHtml: String by lazy {
    val loader = Thread.currentThread().contextClassLoader
    loader.getResource("static/index.html")!!
        .readText(Charsets.UTF_8)
}

fun Application.server(compiler: ResumeCompiler) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText(indexHtml, ContentType.Text.Html)
        }
        get("/bench") {
            call.respondRedirect("/static/bench.html")
        }
        staticResources("/static", "static")
        post("/api/compile") {
            handleCompile(call, compiler)
        }
        post("/api/bench") {
            handleBench(call)
        }
        post("/api/export") {
            handleExportPdf(call, compiler)
        }
    }
}

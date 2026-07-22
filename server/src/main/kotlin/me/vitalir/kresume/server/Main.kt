package me.vitalir.kresume.server

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import me.vitalir.kresume.server.compiler.Jsr223Compiler
import me.vitalir.kresume.server.compiler.KctForkCompiler
import me.vitalir.kresume.server.compiler.RawCompiler
import me.vitalir.kresume.server.compiler.ResumeCompiler

fun main(args: Array<String>) {
    val engine = args.firstOrNull()
        ?.removePrefix("--engine=")
        ?: "raw"

    val compiler: ResumeCompiler = when (engine) {
        "jsr223" -> Jsr223Compiler()
        "kctfork" -> KctForkCompiler()
        "raw" -> RawCompiler()
        else -> throw IllegalArgumentException("Unknown engine: $engine. Valid: jsr223, kctfork, raw")
    }

    Runtime.getRuntime().addShutdownHook(Thread { compiler.close() })

    try {
        embeddedServer(Netty, port = 8080) {
            server(compiler)
        }.start(wait = true)
    } catch (e: Exception) {
        System.err.println("Failed to start server on port 8080: ${e.message}")
        System.exit(1)
    }
}

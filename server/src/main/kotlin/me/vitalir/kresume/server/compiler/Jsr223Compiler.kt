package me.vitalir.kresume.server.compiler

import me.vitalir.kresume.server.model.Resume
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class Jsr223Compiler : ResumeCompiler {

    private val manager = ScriptEngineManager(Jsr223Compiler::class.java.classLoader)

    private fun createEngine(): ScriptEngine =
        manager.getEngineByName("kotlin")
            ?: throw IllegalStateException("Script engine 'kotlin' not found.")

    private fun wrapScript(dslCode: String): String =
        "import me.vitalir.kresume.server.dsl.*\n$dslCode"

    override fun compile(dslCode: String): CompileResult {
        val startNanos = System.nanoTime()
        val engine = createEngine()
        return try {
            val script = wrapScript(dslCode)
            val result = engine.eval(script)
            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0
            when (result) {
                is Resume -> CompileResult.Success(resume = result, compileTimeMs = elapsedMs)
                else -> CompileResult.Error(
                    message = "Expected Resume but got ${(result as? Any)?.let { it::class.qualifiedName } ?: "null"}",
                    line = null,
                    col = null
                )
            }
        } catch (e: ScriptException) {
            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0
            CompileResult.Error(
                message = e.message ?: "Script evaluation failed",
                line = e.lineNumber.takeIf { it >= 0 },
                col = e.columnNumber.takeIf { it >= 0 }
            )
        } catch (e: Exception) {
            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0
            CompileResult.Error(
                message = e.message ?: "Unexpected error during compilation"
            )
        }
    }

    override fun close() = Unit
}

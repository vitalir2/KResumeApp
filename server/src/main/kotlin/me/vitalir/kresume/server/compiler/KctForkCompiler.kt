@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)
package me.vitalir.kresume.server.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import me.vitalir.kresume.server.model.Resume
import java.io.File
import java.nio.file.Files

private const val GENERATED_OBJECT_NAME = "__Gen"
private const val GENERATED_METHOD_NAME = "generate"
private const val GENERATED_PACKAGE = "__compiled__"

private val SOURCE_TEMPLATE: String = """
package $GENERATED_PACKAGE

import me.vitalir.kresume.server.dsl.*
import me.vitalir.kresume.server.model.Resume

object $GENERATED_OBJECT_NAME {
    fun $GENERATED_METHOD_NAME(): Resume = %s
}
""".trimIndent()

class KctForkCompiler : ResumeCompiler {

    private val workingDir: File =
        Files.createTempDirectory("kresume-kctfork-").toFile()

    override fun compile(dslCode: String): CompileResult {
        val startNanos = System.nanoTime()
        return try {
            val sourceContent = SOURCE_TEMPLATE.format(dslCode)
            val sourceFile = SourceFile.kotlin("__Gen.kt", sourceContent)

            val result = KotlinCompilation().apply {
                sources = listOf(sourceFile)
                workingDir = this@KctForkCompiler.workingDir
                inheritClassPath = true
                verbose = false
            }.compile()

            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0

            when (result.exitCode) {
                ExitCode.OK -> {
                    val classLoader = result.classLoader
                    invokeGenerated(classLoader, elapsedMs)
                }
                else -> CompileResult.Error(
                    message = result.messages.ifBlank { "Compilation failed with exit code ${result.exitCode}" }
                )
            }
        } catch (e: Exception) {
            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0
            CompileResult.Error(
                message = e.message ?: "Unexpected error during compilation"
            )
        }
    }

    private fun invokeGenerated(classLoader: ClassLoader, elapsedMs: Double): CompileResult {
        return try {
            val clazz = classLoader.loadClass("$GENERATED_PACKAGE.$GENERATED_OBJECT_NAME")
            val instance = clazz.getDeclaredField("INSTANCE").get(null)
            val method = clazz.getDeclaredMethod(GENERATED_METHOD_NAME)
            val resume = method.invoke(instance) as? Resume
                ?: return CompileResult.Error(
                    message = "Compiled code did not return a Resume instance"
                )
            CompileResult.Success(resume = resume, compileTimeMs = elapsedMs)
        } catch (e: Exception) {
            CompileResult.Error(
                message = "Failed to invoke compiled code: ${e.message}"
            )
        }
    }

    override fun close() {
        workingDir.deleteRecursively()
    }

}

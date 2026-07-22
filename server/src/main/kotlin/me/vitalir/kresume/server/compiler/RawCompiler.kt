package me.vitalir.kresume.server.compiler

import me.vitalir.kresume.server.model.Resume
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.logging.Logger

private const val GENERATED_OBJECT_NAME = "__Gen"
private const val GENERATED_METHOD_NAME = "generate"
private const val GENERATED_PACKAGE = "__compiled__"
private const val SOURCE_FILE_NAME = "$GENERATED_PACKAGE.kt"

private val SOURCE_TEMPLATE: String = """
package $GENERATED_PACKAGE

import me.vitalir.kresume.server.dsl.*
import me.vitalir.kresume.server.model.Resume

object $GENERATED_OBJECT_NAME {
    fun $GENERATED_METHOD_NAME(): Resume = %s
}
""".trimIndent()

class RawCompiler : ResumeCompiler {

    private val logger = Logger.getLogger(RawCompiler::class.java.name)

    private val compiler = K2JVMCompiler()

    private val classpath: String = resolveClasspath()

    private val outputDir: File =
        Files.createTempDirectory("kresume-raw-").toFile().also { dir ->
            dir.resolve("$GENERATED_PACKAGE/").mkdirs()
            logger.info("Output dir: ${dir.absolutePath}")
        }

    private val sourceFile: File = outputDir.resolve(SOURCE_FILE_NAME)

    init {
        logger.info("Classpath resolved (${classpath.length} chars)")
    }

    override fun compile(dslCode: String): CompileResult {
        logger.fine("Compiling DSL (${dslCode.length} chars, ${dslCode.lines().size} lines)")
        val startNanos = System.nanoTime()
        return try {
            // Overwrite source with fresh content
            val sourceContent = SOURCE_TEMPLATE.format(dslCode)
            sourceFile.writeText(sourceContent)

            // Clean previous compiled classes
            cleanOutput()

            val args = K2JVMCompilerArguments().apply {
                classpath = this@RawCompiler.classpath
                destination = this@RawCompiler.outputDir.absolutePath
                freeArgs = listOf(this@RawCompiler.sourceFile.absolutePath)
                // All deps already on classpath — skip redundant auto-adding
                noStdlib = true
                noReflect = true
                noJdk = true
                suppressWarnings = true
            }

            val messageCollector = CollectingMessageCollector()
            val exitCode = compiler.exec(messageCollector, Services.EMPTY, args)
            val elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0

            when (exitCode) {
                ExitCode.OK -> {
                    logger.fine("Compiled in ${"%.1f".format(elapsedMs)}ms")
                    invokeGenerated(elapsedMs)
                }
                else -> {
                    val msg = messageCollector.messages.ifBlank {
                        "Compilation failed with exit code: ${exitCode.name}"
                    }
                    logger.warning("Compilation failed: $msg")
                    CompileResult.Error(message = msg)
                }
            }
        } catch (e: Exception) {
            logger.severe("Compilation error: ${e.message}")
            CompileResult.Error(message = e.message ?: "Unexpected error during compilation")
        }
    }

    private fun invokeGenerated(elapsedMs: Double): CompileResult {
        return try {
            val classLoader = URLClassLoader(
                arrayOf(outputDir.toURI().toURL()),
                RawCompiler::class.java.classLoader
            )
            val clazz = classLoader.loadClass("$GENERATED_PACKAGE.$GENERATED_OBJECT_NAME")
            val instance = clazz.getDeclaredField("INSTANCE").get(null)
            val method = clazz.getDeclaredMethod(GENERATED_METHOD_NAME)
            val resume = method.invoke(instance) as? Resume
                ?: return CompileResult.Error(
                    message = "Compiled code did not return a Resume instance"
                )
            CompileResult.Success(resume = resume, compileTimeMs = elapsedMs)
        } catch (e: Exception) {
            logger.severe("Failed to invoke compiled class: ${e.message}")
            CompileResult.Error(message = "Failed to invoke compiled code: ${e.message}")
        }
    }

    override fun close() {
        logger.info("Cleaning up $outputDir")
        outputDir.deleteRecursively()
    }

    companion object {
        private fun resolveClasspath(): String {
            val classPathProperty = System.getProperty("java.class.path", "")
            if (classPathProperty.isNotBlank()) return classPathProperty

            val contextLoader = Thread.currentThread().contextClassLoader
            if (contextLoader is URLClassLoader) {
                return contextLoader.urLs
                    .map { File(it.toURI()).absolutePath }
                    .joinToString(File.pathSeparator)
            }
            return ""
        }
    }

    private class CollectingMessageCollector : MessageCollector {
        private val buffer = StringBuilder()

        val messages: String get() = buffer.toString()

        override fun clear() { buffer.clear() }

        override fun hasErrors(): Boolean = buffer.isNotEmpty()

        override fun report(
            severity: CompilerMessageSeverity,
            message: String,
            location: CompilerMessageSourceLocation?
        ) {
            if (severity.isError || severity == CompilerMessageSeverity.EXCEPTION) {
                val loc = location?.let { "${it.line}:${it.column}" } ?: "unknown"
                buffer.appendLine("[$severity] $loc: $message")
            }
        }
    }

    private fun cleanOutput() {
        val pkgDir = outputDir.resolve(GENERATED_PACKAGE)
        if (pkgDir.isDirectory) {
            pkgDir.listFiles()?.forEach { it.delete() }
        }
    }
}

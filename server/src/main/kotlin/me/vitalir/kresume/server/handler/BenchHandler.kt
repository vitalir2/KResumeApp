package me.vitalir.kresume.server.handler

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable
import me.vitalir.kresume.server.compiler.Jsr223Compiler
import me.vitalir.kresume.server.compiler.KctForkCompiler
import me.vitalir.kresume.server.compiler.RawCompiler
import me.vitalir.kresume.server.compiler.ResumeCompiler
import java.lang.management.ManagementFactory
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

@Serializable
data class BenchStats(
    val p50: Double,
    val p95: Double,
    val p99: Double,
    val mean: Double,
    val stdDev: Double,
    val min: Double,
    val max: Double
)

@Serializable
data class BenchResult(
    val engine: String,
    val size: Int,
    val coldStartMs: Double,
    val hot: BenchStats,
    val heapDeltaKb: Long,
    val gcPauseMs: Long
)

private fun createCompiler(engine: String): ResumeCompiler = when (engine) {
    "jsr223" -> Jsr223Compiler()
    "kctfork" -> KctForkCompiler()
    "raw" -> RawCompiler()
    else -> throw IllegalArgumentException("Unknown engine: $engine")
}

private fun percentile(sorted: List<Double>, p: Int): Double {
    val k = (p / 100.0) * (sorted.size - 1)
    val f = k.toInt()
    val c = k - f
    return if (f + 1 < sorted.size) {
        sorted[f] * (1 - c) + sorted[f + 1] * c
    } else {
        sorted[f]
    }
}

suspend fun handleBench(call: ApplicationCall) {
    val engines = listOf("jsr223", "kctfork", "raw")
    val sizes = listOf(50, 200, 500, 2000)

    val results = mutableListOf<BenchResult>()

    for (engine in engines) {
        for (size in sizes) {
            val resourcePath = "bench/basics_${size}.txt"
            val dsl = call::class.java.classLoader.getResource(resourcePath)
                ?.readText()
                ?: throw IllegalStateException("Resource not found: $resourcePath")

            // Cold compile — fresh compiler, single compile, close
            val coldStartMs = measureTimeMillis {
                val compiler = createCompiler(engine)
                try {
                    compiler.compile(dsl)
                } finally {
                    compiler.close()
                }
            }

            // Heap and GC state before hot compiles
            val heapBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
            val gcBeans = ManagementFactory.getGarbageCollectorMXBeans()
            val gcBefore = gcBeans.sumOf { it.collectionTime }

            // Hot compiles — 50 iterations with one compiler instance
            val hotTimes = mutableListOf<Double>()
            val hotCompiler = createCompiler(engine)
            try {
                repeat(50) {
                    val start = System.nanoTime()
                    hotCompiler.compile(dsl)
                    hotTimes.add((System.nanoTime() - start) / 1_000_000.0)
                }
            } finally {
                hotCompiler.close()
            }

            // Heap and GC state after hot compiles
            val heapAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
            val gcAfter = gcBeans.sumOf { it.collectionTime }

            val heapDeltaKb = (heapAfter - heapBefore) / 1024
            val gcPauseMs = gcAfter - gcBefore

            // Statistics
            val sorted = hotTimes.sorted()
            val mean = hotTimes.average()
            val variance = hotTimes.map { (it - mean) * (it - mean) }.average()
            val stdDev = sqrt(variance)

            val stats = BenchStats(
                p50 = percentile(sorted, 50),
                p95 = percentile(sorted, 95),
                p99 = percentile(sorted, 99),
                mean = mean,
                stdDev = stdDev,
                min = sorted.first(),
                max = sorted.last()
            )

            results.add(
                BenchResult(
                    engine = engine,
                    size = size,
                    coldStartMs = coldStartMs.toDouble(),
                    hot = stats,
                    heapDeltaKb = heapDeltaKb,
                    gcPauseMs = gcPauseMs
                )
            )
        }
    }

    call.respond(results)
}

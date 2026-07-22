package me.vitalir.kresume.server.compiler

import me.vitalir.kresume.server.model.Resume

sealed class CompileResult {
    data class Success(val resume: Resume, val compileTimeMs: Double) : CompileResult()
    data class Error(val message: String, val line: Int? = null, val col: Int? = null) : CompileResult()
}

interface ResumeCompiler {
    fun compile(dslCode: String): CompileResult
    fun close()
}

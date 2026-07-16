package me.vitalir.kresume.linter.konsist.rules

import me.vitalir.kresume.linter.konsist.ProjectFiles
import me.vitalir.kresume.linter.konsist.assertions.prohibitPackage
import me.vitalir.kresume.linter.konsist.assertions.prohibitPackageExcept
import kotlin.test.Test

class TestingAssertionsRule {
    @Test
    fun `no junit assertions allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "org.junit.Assert",
            message = "org.junit.Assert is forbidden. Use io.kotest.matchers instead — see docs/code-style.md.",
        )

    @Test
    fun `no hamcrest matchers allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "org.hamcrest",
            message = "org.hamcrest is forbidden. Use io.kotest.matchers instead — see docs/code-style.md.",
        )

    @Test
    fun `no kotlin test assertions allowed`() =
        ProjectFiles.tests.prohibitPackageExcept(
            prefix = "kotlin.test",
            allowed =
                setOf(
                    "kotlin.test.Test",
                    "kotlin.test.BeforeTest",
                    "kotlin.test.AfterTest",
                    "kotlin.test.Ignore",
                ),
            message = "kotlin.test assertion functions are forbidden. Use io.kotest.matchers instead.",
        )
}

package me.vitalir.kmptemplate.linter.konsist.rules

import me.vitalir.kmptemplate.linter.konsist.ProjectFiles
import me.vitalir.kmptemplate.linter.konsist.assertions.prohibitExact
import kotlin.test.Test

class TestingAnnotationsRule {
    @Test
    fun `no junit test annotations allowed`() =
        ProjectFiles.tests.prohibitExact(
            name = "org.junit.Test",
            message = "org.junit.Test is forbidden. Use @kotlin.test.Test instead.",
        )

    @Test
    fun `no junit before annotation allowed`() =
        ProjectFiles.tests.prohibitExact(
            name = "org.junit.Before",
            message = "org.junit.Before is forbidden. Use @kotlin.test.BeforeTest instead.",
        )

    @Test
    fun `no junit after annotation allowed`() =
        ProjectFiles.tests.prohibitExact(
            name = "org.junit.After",
            message = "org.junit.After is forbidden. Use @kotlin.test.AfterTest instead.",
        )

    @Test
    fun `no junit ignore annotation allowed`() =
        ProjectFiles.tests.prohibitExact(
            name = "org.junit.Ignore",
            message = "org.junit.Ignore is forbidden. Use @kotlin.test.Ignore instead.",
        )
}

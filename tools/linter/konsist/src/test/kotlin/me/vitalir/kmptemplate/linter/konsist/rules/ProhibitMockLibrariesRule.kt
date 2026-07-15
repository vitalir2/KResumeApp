package me.vitalir.kmptemplate.linter.konsist.rules

import me.vitalir.kmptemplate.linter.konsist.ProjectFiles
import me.vitalir.kmptemplate.linter.konsist.assertions.prohibitPackage
import kotlin.test.Test

class ProhibitMockLibrariesRule {
    @Test
    fun `no mockk imports allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "io.mockk",
            message = "MockK (io.mockk) is forbidden. Use manual fakes instead.",
        )

    @Test
    fun `no mockito imports allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "org.mockito",
            message = "Mockito (org.mockito) is forbidden. Use manual fakes instead.",
        )

    @Test
    fun `no easymock imports allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "org.easymock",
            message = "EasyMock (org.easymock) is forbidden. Use manual fakes instead.",
        )

    @Test
    fun `no powermock imports allowed`() =
        ProjectFiles.tests.prohibitPackage(
            prefix = "org.powermock",
            message = "PowerMock (org.powermock) is forbidden. Use manual fakes instead.",
        )
}

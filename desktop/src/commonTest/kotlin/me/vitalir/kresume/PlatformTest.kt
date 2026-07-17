package me.vitalir.kresume

import io.kotest.matchers.string.shouldNotBeEmpty
import kotlin.test.Test

class PlatformTest {
    @Test
    fun `platform name is non-empty`() {
        platformName().shouldNotBeEmpty()
    }
}

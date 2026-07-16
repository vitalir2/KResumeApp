package me.vitalir.kresume.linter.konsist

import com.lemonappdev.konsist.api.Konsist

/**
 * Pre-configured Konsist scopes for the project's source sets and modules.
 * Each scope is lazy-initialized once to avoid redundant filesystem scans.
 *
 * @see tests scans `test`, `androidTest`, and KMP `commonTest` source sets
 * @see main scans the root module for full-project rules
 */
object ProjectFiles {
    val tests by lazy { Konsist.scopeFromSourceSet("test", "androidTest", "commonTest") }

    val main by lazy { Konsist.scopeFromModule(":") }
}

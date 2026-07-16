package me.vitalir.kresume.linter.konsist.assertions

import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.verify.assertEmpty

fun KoScope.prohibitPackage(
    prefix: String,
    message: String,
) {
    imports
        .filter { it.name.startsWith(prefix) }
        .assertEmpty(additionalMessage = message)
}

fun KoScope.prohibitExact(
    name: String,
    message: String,
) {
    imports
        .filter { it.name == name }
        .assertEmpty(additionalMessage = message)
}

fun KoScope.prohibitPackageExcept(
    prefix: String,
    allowed: Set<String>,
    message: String,
) {
    imports
        .filter { it.name.startsWith(prefix) && it.name !in allowed }
        .assertEmpty(additionalMessage = message)
}

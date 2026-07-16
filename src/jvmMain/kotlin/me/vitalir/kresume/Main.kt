package me.vitalir.kresume

import androidx.compose.ui.window.application

/**
 * JVM/desktop entry point.
 * Launches the shared [App] composable in a native window.
 */
fun main() =
    application {
        App()
    }

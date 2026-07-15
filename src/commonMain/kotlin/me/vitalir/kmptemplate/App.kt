package me.vitalir.kmptemplate

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Root composable for the shared UI layer.
 * Use as the entry composable on each platform.
 */
@Composable
fun App() {
    MaterialTheme {
        Column {
            Text("Hello from KMP Compose Multiplatform!")
            Text("Platform: ${platformName()}")
        }
    }
}

package me.vitalir.kresume

import androidx.compose.ui.window.ComposeUIViewController

/**
 * iOS entry point. Called from Swift to create the UIViewController.
 */
fun MainViewController() = ComposeUIViewController { App() }

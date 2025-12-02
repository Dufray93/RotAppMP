package com.aplimovil.rotappmp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.aplimovil.rotappmp.di.DefaultAppContainer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appContainer = DefaultAppContainer()
    ComposeViewport {
        App(appContainer = appContainer)
    }
}
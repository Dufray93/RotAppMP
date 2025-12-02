package com.aplimovil.rotappmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.aplimovil.rotappmp.di.DefaultAppContainer

fun main() = application {
    val appContainer = DefaultAppContainer()
    Window(
        onCloseRequest = ::exitApplication,
        title = "RotAppMP",
    ) {
        App(appContainer = appContainer)
    }
}
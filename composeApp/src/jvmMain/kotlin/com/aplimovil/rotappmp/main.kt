package com.aplimovil.rotappmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RotAppMP",
    ) {
        App()
    }
}
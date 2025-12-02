package com.aplimovil.rotappmp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.aplimovil.rotappmp.di.DefaultAppContainer

fun MainViewController() = ComposeUIViewController {
    val appContainer = remember { DefaultAppContainer() }
    App(appContainer = appContainer)
}

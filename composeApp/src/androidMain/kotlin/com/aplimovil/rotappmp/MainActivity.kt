package com.aplimovil.rotappmp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview
import com.aplimovil.rotappmp.data.local.initSettings
import com.aplimovil.rotappmp.data.repository.FakeCompanyRepository
import com.aplimovil.rotappmp.data.repository.FakeUserRepository
import com.aplimovil.rotappmp.di.AppContainer
import com.aplimovil.rotappmp.di.DefaultAppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate() called")

        initSettings(applicationContext)

        val appContainer = DefaultAppContainer()

        setContent {
            LoggedApp(appContainer = appContainer)
        }
    }
}

@Composable
private fun LoggedApp(appContainer: AppContainer) {
    SideEffect { Log.d(TAG, "Compose content rendered") }
    App(appContainer = appContainer)
}

private const val TAG = "MainActivity"

@Preview
@Composable
fun AppAndroidPreview() {
    val previewContainer = object : AppContainer {
        override val userRepository = FakeUserRepository()
        override val companyRepository = FakeCompanyRepository()
    }
    App(appContainer = previewContainer)
}
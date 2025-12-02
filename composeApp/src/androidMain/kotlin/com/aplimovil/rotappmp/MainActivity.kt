package com.aplimovil.rotappmp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate() called")
        setContent {
            LoggedApp()
        }
    }
}

@Composable
private fun LoggedApp() {
    SideEffect { Log.d(TAG, "Compose content rendered") }
    App()
}

private const val TAG = "MainActivity"

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
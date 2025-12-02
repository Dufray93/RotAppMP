package com.aplimovil.rotappmp.data.local

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

private const val SETTINGS_NAME = "rotapp_settings"

private var appContext: Context? = null

/**
 * Debe llamarse al iniciar la aplicación para registrar el contexto de Android.
 */
fun initSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun createSettings(): Settings {
    val context = appContext
        ?: throw IllegalStateException("initSettings(context) must be called before using LocalStorage on Android.")
    val sharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}

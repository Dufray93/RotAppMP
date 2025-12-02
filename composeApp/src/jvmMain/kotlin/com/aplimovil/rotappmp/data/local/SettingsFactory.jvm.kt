package com.aplimovil.rotappmp.data.local

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

private const val PREFERENCES_NODE = "com.aplimovil.rotappmp"

actual fun createSettings(): Settings {
    val preferences = Preferences.userRoot().node(PREFERENCES_NODE)
    return PreferencesSettings(preferences)
}

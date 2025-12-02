package com.aplimovil.rotappmp.data.local

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual fun createSettings(): Settings {
    val defaults = NSUserDefaults.standardUserDefaults()
    return NSUserDefaultsSettings(defaults)
}

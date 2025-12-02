package com.aplimovil.rotappmp.data.local

import com.russhwolf.settings.Settings

/**
 * Provee una instancia de [Settings] adecuada para la plataforma en ejecución.
 */
expect fun createSettings(): Settings

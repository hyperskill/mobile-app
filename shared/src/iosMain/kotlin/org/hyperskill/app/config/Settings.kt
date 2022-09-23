package org.hyperskill.app.config

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings

import platform.Foundation.NSUserDefaults

object Settings {
    fun makeAppleSettings(userDefaults: NSUserDefaults): Settings =
        AppleSettings(userDefaults)
}
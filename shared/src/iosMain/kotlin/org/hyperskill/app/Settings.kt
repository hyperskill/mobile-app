package org.hyperskill.app

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings

import platform.Foundation.NSUserDefaults

object Settings {
    fun makeAppleSettings(userDefaults: NSUserDefaults): Settings =
        AppleSettings(userDefaults)
}
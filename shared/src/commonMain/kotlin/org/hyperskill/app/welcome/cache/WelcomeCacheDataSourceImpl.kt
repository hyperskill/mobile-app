package org.hyperskill.app.welcome.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.welcome.data.source.WelcomeCacheDataSource

internal class WelcomeCacheDataSourceImpl(
    private val settings: Settings
) : WelcomeCacheDataSource {
    override fun isWelcomeScreenShown(): Boolean =
        settings.getBoolean(
            key = WelcomeCacheKeys.IS_ONBOARDING_SHOWN,
            defaultValue = false
        )

    override fun setWelcomeScreenShown(isShown: Boolean) {
        settings.putBoolean(
            key = WelcomeCacheKeys.IS_ONBOARDING_SHOWN,
            value = isShown
        )
    }
}
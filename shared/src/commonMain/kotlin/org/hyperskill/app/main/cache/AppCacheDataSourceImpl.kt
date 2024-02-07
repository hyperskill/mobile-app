package org.hyperskill.app.main.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.main.data.source.AppCacheDataSource

internal class AppCacheDataSourceImpl(
    private val settings: Settings
) : AppCacheDataSource {
    override fun isAppDidLaunchFirstTime(): Boolean =
        settings.getBoolean(AppCacheKeyValues.APP_DID_LAUNCH_FIRST_TIME, defaultValue = false)

    override fun setAppDidLaunchFirstTime() {
        settings.putBoolean(AppCacheKeyValues.APP_DID_LAUNCH_FIRST_TIME, true)
    }
}
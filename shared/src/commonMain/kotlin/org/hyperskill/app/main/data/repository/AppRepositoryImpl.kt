package org.hyperskill.app.main.data.repository

import org.hyperskill.app.main.data.source.AppCacheDataSource
import org.hyperskill.app.main.domain.repository.AppRepository

internal class AppRepositoryImpl(
    private val appCacheDataSource: AppCacheDataSource
) : AppRepository {
    override fun isAppDidLaunchFirstTime(): Boolean =
        appCacheDataSource.isAppDidLaunchFirstTime()

    override fun setAppDidLaunchFirstTime() {
        appCacheDataSource.setAppDidLaunchFirstTime()
    }
}
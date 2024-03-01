package org.hyperskill.app.main.data.source

interface AppCacheDataSource {
    fun isAppDidLaunchFirstTime(): Boolean
    fun setAppDidLaunchFirstTime()
}
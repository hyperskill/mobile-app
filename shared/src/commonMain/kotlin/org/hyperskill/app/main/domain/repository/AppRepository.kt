package org.hyperskill.app.main.domain.repository

interface AppRepository {
    fun isAppDidLaunchFirstTime(): Boolean
    fun setAppDidLaunchFirstTime()
}
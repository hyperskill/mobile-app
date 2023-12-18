package org.hyperskill.app.welcome.data.source

interface WelcomeCacheDataSource {
    fun isWelcomeScreenShown(): Boolean
    fun setWelcomeScreenShown(isShown: Boolean)
}
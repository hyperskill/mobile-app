package org.hyperskill.app.welcome.domain.repository

interface WelcomeRepository {
    fun isWelcomeScreenShown(): Boolean
    fun setWelcomeScreenShown(isShown: Boolean)
}
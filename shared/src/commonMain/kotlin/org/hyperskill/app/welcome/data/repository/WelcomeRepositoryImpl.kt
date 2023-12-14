package org.hyperskill.app.welcome.data.repository

import org.hyperskill.app.welcome.data.source.WelcomeCacheDataSource
import org.hyperskill.app.welcome.domain.repository.WelcomeRepository

internal class WelcomeRepositoryImpl(
    private val welcomeCacheDataSource: WelcomeCacheDataSource
) : WelcomeRepository {

    override fun isWelcomeScreenShown(): Boolean =
        welcomeCacheDataSource.isWelcomeScreenShown()

    override fun setWelcomeScreenShown(isShown: Boolean) {
        welcomeCacheDataSource.setWelcomeScreenShown(isShown)
    }
}
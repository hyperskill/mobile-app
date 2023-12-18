package org.hyperskill.app.welcome.domain.interactor

import org.hyperskill.app.welcome.domain.repository.WelcomeRepository

class WelcomeInteractor(
    private val welcomeRepository: WelcomeRepository
) {
    fun isWelcomeScreenShown(): Boolean =
        welcomeRepository.isWelcomeScreenShown()

    fun setWelcomeScreenShown(isShown: Boolean) {
        welcomeRepository.setWelcomeScreenShown(isShown)
    }
}
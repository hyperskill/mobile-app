package org.hyperskill.app.onboarding.domain.interactor

import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingInteractor(
    private val onboardingRepository: OnboardingRepository
) {
    fun isOnboardingShown(): Boolean =
        onboardingRepository.isOnboardingShown()

    fun setOnboardingShown(isShown: Boolean) {
        onboardingRepository.setOnboardingShown(isShown)
    }

    fun isParsonsOnboardingShown(): Boolean =
        onboardingRepository.isParsonsOnboardingShown()

    fun setParsonsOnboardingShown(isShown: Boolean) {
        onboardingRepository.setParsonsOnboardingShown(isShown)
    }

    fun wasNotificationOnboardingShown(): Boolean =
        onboardingRepository.wasNotificationOnboardingShown()

    fun setNotificationOnboardingWasShown(wasShown: Boolean) {
        onboardingRepository.setNotificationOnboardingWasShown(wasShown)
    }
}
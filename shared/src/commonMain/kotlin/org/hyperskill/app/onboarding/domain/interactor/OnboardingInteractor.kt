package org.hyperskill.app.onboarding.domain.interactor

import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingInteractor(
    private val onboardingRepository: OnboardingRepository
) {
    fun isOnboardingShown(): Boolean =
        onboardingRepository.isOnboardingShown()

    fun setOnboardingShown(showed: Boolean) {
        onboardingRepository.setOnboardingShown(showed)
    }
}
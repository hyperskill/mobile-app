package org.hyperskill.app.onboarding.domain.interactor

import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingInteractor(
    private val onboardingRepository: OnboardingRepository
) {
    fun isOnboardingShowed(): Boolean =
        onboardingRepository.isOnboardingShowed()

    fun setOnboardingShowed(showed: Boolean) {
        onboardingRepository.setOnboardingShowed(showed)
    }
}
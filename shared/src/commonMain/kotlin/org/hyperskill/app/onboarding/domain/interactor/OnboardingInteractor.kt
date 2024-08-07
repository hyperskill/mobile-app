package org.hyperskill.app.onboarding.domain.interactor

import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository
import org.hyperskill.app.onboarding.domain.repository.getProblemsOnboardingFlags

class OnboardingInteractor(
    private val onboardingRepository: OnboardingRepository
) {
    fun setParsonsOnboardingShown(isShown: Boolean) {
        onboardingRepository.setParsonsOnboardingShown(isShown)
    }

    fun getProblemsOnboardingFlags(): ProblemsOnboardingFlags =
        onboardingRepository.getProblemsOnboardingFlags()

    fun wasFirstProblemOnboardingShown(): Boolean =
        onboardingRepository.wasFirstProblemOnboardingShown()

    fun setFirstProblemOnboardingWasShown(wasShown: Boolean) {
        onboardingRepository.setFirstProblemOnboardingWasShown(wasShown)
    }
}
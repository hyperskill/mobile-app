package org.hyperskill.app.onboarding.data.repository

import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

internal class OnboardingRepositoryImpl(
    private val onboardingCacheDataSource: OnboardingCacheDataSource
) : OnboardingRepository {

    override fun isParsonsOnboardingShown(): Boolean =
        onboardingCacheDataSource.isParsonsOnboardingShown()

    override fun setParsonsOnboardingShown(isShown: Boolean) {
        onboardingCacheDataSource.setParsonsOnboardingShown(isShown)
    }

    override fun wasFirstProblemOnboardingShown(): Boolean =
        onboardingCacheDataSource.wasFirstProblemOnboardingShown()

    override fun setFirstProblemOnboardingWasShown(wasShown: Boolean) {
        onboardingCacheDataSource.setFirstProblemOnboardingWasShown(wasShown)
    }
}
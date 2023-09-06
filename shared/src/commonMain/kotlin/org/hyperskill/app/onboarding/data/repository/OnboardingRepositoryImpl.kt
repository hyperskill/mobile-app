package org.hyperskill.app.onboarding.data.repository

import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(
    private val onboardingCacheDataSource: OnboardingCacheDataSource
) : OnboardingRepository {
    override fun isOnboardingShown(): Boolean =
        onboardingCacheDataSource.isOnboardingShown()

    override fun setOnboardingShown(isShown: Boolean) {
        onboardingCacheDataSource.setOnboardingShown(isShown)
    }

    override fun isParsonsOnboardingShown(): Boolean =
        onboardingCacheDataSource.isParsonsOnboardingShown()

    override fun setParsonsOnboardingShown(isShown: Boolean) {
        onboardingCacheDataSource.setParsonsOnboardingShown(isShown)
    }
}
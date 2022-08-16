package org.hyperskill.app.onboarding.data.repository

import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(
    private val onboardingCacheDataSource: OnboardingCacheDataSource
) : OnboardingRepository {
    override fun isOnboardingShown(): Boolean =
        onboardingCacheDataSource.isOnboardingShown()

    override fun setOnboardingShown(showed: Boolean) {
        onboardingCacheDataSource.setOnboardingShown(showed)
    }
}
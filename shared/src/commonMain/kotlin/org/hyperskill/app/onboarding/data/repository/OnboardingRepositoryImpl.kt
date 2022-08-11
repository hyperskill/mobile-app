package org.hyperskill.app.onboarding.data.repository

import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(
    private val onboardingCacheDataSource: OnboardingCacheDataSource
) : OnboardingRepository {
    override fun isOnboardingShowed(): Boolean =
        onboardingCacheDataSource.isOnboardingShowed()

    override fun setOnboardingShowed(showed: Boolean) {
        onboardingCacheDataSource.setOnboardingShowed(showed)
    }
}
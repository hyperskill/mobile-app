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

    override fun isFillBlanksInputModeOnboardingShown(): Boolean =
        onboardingCacheDataSource.isFillBlanksInputModeOnboardingShown()

    override fun setFillBlanksInputModeOnboardingShown(isShown: Boolean) {
        onboardingCacheDataSource.setFillBlanksInputModeOnboardingShown(isShown)
    }

    override fun isFillBlanksSelectModeOnboardingShown(): Boolean =
        onboardingCacheDataSource.isFillBlanksSelectModeOnboardingShown()

    override fun setFillBlanksSelectModeOnboardingShown(isShown: Boolean) {
        onboardingCacheDataSource.setFillBlanksSelectModeOnboardingShown(isShown)
    }

    override fun wasNotificationOnboardingShown(): Boolean =
        onboardingCacheDataSource.wasNotificationOnboardingShown()

    override fun setNotificationOnboardingWasShown(wasShown: Boolean) {
        onboardingCacheDataSource.setNotificationOnboardingWasShown(wasShown)
    }

    override fun wasFirstProblemOnboardingShown(): Boolean =
        onboardingCacheDataSource.wasFirstProblemOnboardingShown()

    override fun setFirstProblemOnboardingWasShown(wasShown: Boolean) {
        onboardingCacheDataSource.setFirstProblemOnboardingWasShown(wasShown)
    }
}
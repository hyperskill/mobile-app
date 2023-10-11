package org.hyperskill.app.onboarding.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource

class OnboardingCacheDataSourceImpl(
    private val settings: Settings
) : OnboardingCacheDataSource {
    override fun isOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWN)

    override fun setOnboardingShown(isShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWN, isShown)
    }

    override fun isParsonsOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_PARSONS_ONBOARDING_SHOWN)

    override fun setParsonsOnboardingShown(isShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_PARSONS_ONBOARDING_SHOWN, isShown)
    }

    override fun wasNotificationOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_NOTIFICATIONS_ONBOARDING_SHOWN, defaultValue = false)

    override fun setNotificationOnboardingWasShown(wasShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_NOTIFICATIONS_ONBOARDING_SHOWN, wasShown)
    }

    override fun wasFirstProblemOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_FIRST_PROBLEM_ONBOARDING_SHOWN, defaultValue = false)

    override fun setFirstProblemOnboardingWasShown(wasShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_FIRST_PROBLEM_ONBOARDING_SHOWN, wasShown)
    }
}
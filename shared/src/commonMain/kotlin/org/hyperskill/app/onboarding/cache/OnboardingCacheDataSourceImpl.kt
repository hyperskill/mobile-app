package org.hyperskill.app.onboarding.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource

internal class OnboardingCacheDataSourceImpl(
    private val settings: Settings
) : OnboardingCacheDataSource {
    override fun isParsonsOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_PARSONS_ONBOARDING_SHOWN)

    override fun setParsonsOnboardingShown(isShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_PARSONS_ONBOARDING_SHOWN, isShown)
    }

    override fun wasFirstProblemOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_FIRST_PROBLEM_ONBOARDING_SHOWN, defaultValue = false)

    override fun setFirstProblemOnboardingWasShown(wasShown: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_FIRST_PROBLEM_ONBOARDING_SHOWN, wasShown)
    }
}
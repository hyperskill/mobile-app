package org.hyperskill.app.onboarding.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource

class OnboardingCacheDataSourceImpl(
    private val settings: Settings
) : OnboardingCacheDataSource {
    override fun isOnboardingShown(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWN)

    override fun setOnboardingShown(showed: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWN, showed)
    }
}
package org.hyperskill.app.onboarding.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource

class OnboardingCacheDataSourceImpl(
    private val settings: Settings
) : OnboardingCacheDataSource {
    override fun isOnboardingShowed(): Boolean =
        settings.getBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWED)

    override fun setOnboardingShowed(showed: Boolean) {
        settings.putBoolean(OnboardingCacheKeyValues.IS_ONBOARDING_SHOWED, showed)
    }
}
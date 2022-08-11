package org.hyperskill.app.onboarding.data.source

interface OnboardingCacheDataSource {
    fun isOnboardingShowed(): Boolean
    fun setOnboardingShowed(showed: Boolean)
}
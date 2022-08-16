package org.hyperskill.app.onboarding.data.source

interface OnboardingCacheDataSource {
    fun isOnboardingShown(): Boolean
    fun setOnboardingShown(showed: Boolean)
}
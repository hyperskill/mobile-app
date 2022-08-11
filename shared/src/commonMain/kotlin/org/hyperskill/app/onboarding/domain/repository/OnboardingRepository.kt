package org.hyperskill.app.onboarding.domain.repository

interface OnboardingRepository {
    fun isOnboardingShowed(): Boolean
    fun setOnboardingShowed(showed: Boolean)
}
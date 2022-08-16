package org.hyperskill.app.onboarding.domain.repository

interface OnboardingRepository {
    fun isOnboardingShown(): Boolean
    fun setOnboardingShown(showed: Boolean)
}
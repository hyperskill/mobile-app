package org.hyperskill.app.onboarding.domain.repository

interface OnboardingRepository {
    fun isOnboardingShown(): Boolean
    fun setOnboardingShown(isShown: Boolean)
    fun isParsonsOnboardingShown(): Boolean
    fun setParsonsOnboardingShown(isShown: Boolean)

    fun wasNotificationOnboardingShown(): Boolean
    fun setNotificationOnboardingWasShown(wasShown: Boolean)

    fun wasFirstProblemOnboardingShown(): Boolean
    fun setFirstProblemOnboardingWasShown(wasShown: Boolean)
}
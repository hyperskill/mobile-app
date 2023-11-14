package org.hyperskill.app.onboarding.data.source

interface OnboardingCacheDataSource {
    fun isOnboardingShown(): Boolean
    fun setOnboardingShown(isShown: Boolean)

    fun isParsonsOnboardingShown(): Boolean
    fun setParsonsOnboardingShown(isShown: Boolean)

    fun isFillBlanksInputModeOnboardingShown(): Boolean
    fun setFillBlanksInputModeOnboardingShown(isShown: Boolean)
    fun isFillBlanksSelectModeOnboardingShown(): Boolean
    fun setFillBlanksSelectModeOnboardingShown(isShown: Boolean)

    fun wasNotificationOnboardingShown(): Boolean
    fun setNotificationOnboardingWasShown(wasShown: Boolean)

    fun wasFirstProblemOnboardingShown(): Boolean
    fun setFirstProblemOnboardingWasShown(wasShown: Boolean)
}
package org.hyperskill.app.onboarding.data.source

interface OnboardingCacheDataSource {
    fun isParsonsOnboardingShown(): Boolean
    fun setParsonsOnboardingShown(isShown: Boolean)

    fun isFillBlanksInputModeOnboardingShown(): Boolean
    fun setFillBlanksInputModeOnboardingShown(isShown: Boolean)
    fun isFillBlanksSelectModeOnboardingShown(): Boolean
    fun setFillBlanksSelectModeOnboardingShown(isShown: Boolean)

    fun isGptCodeGenerationWithErrorsOnboardingShown(): Boolean
    fun setGptCodeGenerationWithErrorsOnboardingShown(isShown: Boolean)

    fun wasFirstProblemOnboardingShown(): Boolean
    fun setFirstProblemOnboardingWasShown(wasShown: Boolean)
}
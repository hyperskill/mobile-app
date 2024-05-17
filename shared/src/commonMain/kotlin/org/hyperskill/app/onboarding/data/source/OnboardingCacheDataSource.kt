package org.hyperskill.app.onboarding.data.source

interface OnboardingCacheDataSource {
    fun isParsonsOnboardingShown(): Boolean
    fun setParsonsOnboardingShown(isShown: Boolean)

    fun isGptCodeGenerationWithErrorsOnboardingShown(): Boolean
    fun setGptCodeGenerationWithErrorsOnboardingShown(isShown: Boolean)

    fun wasFirstProblemOnboardingShown(): Boolean
    fun setFirstProblemOnboardingWasShown(wasShown: Boolean)
}
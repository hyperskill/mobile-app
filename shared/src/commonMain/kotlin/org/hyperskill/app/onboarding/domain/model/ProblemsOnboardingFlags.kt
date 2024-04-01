package org.hyperskill.app.onboarding.domain.model

class ProblemsOnboardingFlags(
    val isParsonsOnboardingShown: Boolean,
    val isFillBlanksInputModeOnboardingShown: Boolean,
    val isFillBlanksSelectModeOnboardingShown: Boolean,
    val isGptCodeGenerationWithErrorsOnboardingShown: Boolean
) {
    companion object
}
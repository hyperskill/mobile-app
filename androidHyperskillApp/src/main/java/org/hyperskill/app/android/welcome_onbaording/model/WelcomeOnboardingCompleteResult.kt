package org.hyperskill.app.android.welcome_onbaording.model

sealed interface WelcomeOnboardingCompleteResult {
    object Empty : WelcomeOnboardingCompleteResult
    data class StepRoute(
        val stepRoute: org.hyperskill.app.step.domain.model.StepRoute
    ) : WelcomeOnboardingCompleteResult
}
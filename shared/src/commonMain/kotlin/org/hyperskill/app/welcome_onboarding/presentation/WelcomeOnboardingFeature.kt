package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStep

object WelcomeOnboardingFeature {
    data class State(val currentStep: WelcomeOnboardingStep)

    internal fun initialState() =
        State(currentStep = WelcomeOnboardingStep.START_SCREEN)

    sealed interface Message

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}
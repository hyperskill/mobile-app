package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

interface OnboardingFeature {
    sealed interface State {
        object Content : State
    }

    sealed interface Message {
        object Initialize : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSignInEventMessage : Message
        object ClickedSignUnEventMessage : Message
    }

    sealed interface Action {
        object FetchOnboarding : Action
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
        sealed class ViewAction : Action
    }
}
package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile

interface OnboardingFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object NetworkError : State
        data class Content(val isAuthorized: Boolean) : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        data class OnboardingSuccess(val profile: Profile) : Message
        object OnboardingFailure : Message

        object ClickedSignUn : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSignInEventMessage : Message
    }

    sealed interface Action {
        object FetchOnboarding : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class AuthScreen(val isInSignUpMode: Boolean) : NavigateTo
                object NewUserScreen : NavigateTo
            }
        }
    }
}
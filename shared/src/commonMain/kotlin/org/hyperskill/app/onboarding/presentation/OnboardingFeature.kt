package org.hyperskill.app.onboarding.presentation

interface OnboardingFeature {
    sealed interface State {
        object Content : State
    }

    sealed interface Message {
        object Init : Message
    }

    sealed interface Action {
        object FetchOnboarding : Action
        sealed class ViewAction : Action
    }
}
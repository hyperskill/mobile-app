package org.hyperskill.app.notification_onboarding.presentation

object NotificationOnboardingFeature {
    object State

    sealed interface Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }
}
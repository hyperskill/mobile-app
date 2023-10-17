package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object NotificationsOnboardingFeature {
    object State

    sealed interface Message {
        object AllowNotificationClicked : Message
        object RemindMeLaterClicked : Message
        data class NotificationPermissionRequestResult(val isPermissionGranted: Boolean) : Message
        object ViewedEventMessage : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object RequestNotificationPermission : ViewAction
            object CompleteNotificationOnboarding : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
        object UpdateLastNotificationPermissionRequestTime : InternalAction
    }
}
package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent

object NotificationsOnboardingFeature {
    object State

    sealed interface Message {
        object AllowNotificationClicked : Message
        object RemindMeLaterClicked : Message
        data class NotificationPermissionRequestResult(val isGranted: Boolean) : Message
        object ViewedEventMessage : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object RequestNotificationPermission : ViewAction
            object CompleteNotificationOnboarding : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticsEvent(val event: HyperskillAnalyticEvent) : Action
        object UpdateLastNotificationPermissionRequestTime : Action
    }
}
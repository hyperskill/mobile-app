package org.hyperskill.app.notification_onboarding.presentation

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

        data class LogAnalyticsEvent(val event: HyperskillAnalyticEvent) : Action
        object UpdateLastNotificationPermissionRequestTime : Action
        sealed interface ViewAction : Action {
            object RequestNotificationPermission : Action
            object CompleteNotificationOnboarding : ViewAction
        }
    }
}
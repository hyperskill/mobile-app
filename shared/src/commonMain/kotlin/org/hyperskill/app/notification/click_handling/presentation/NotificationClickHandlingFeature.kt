package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

object NotificationClickHandlingFeature {
    object State

    sealed interface Message {
        data class NotificationClicked(
            val notificationData: PushNotificationData
        ) : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction
        }
    }
}
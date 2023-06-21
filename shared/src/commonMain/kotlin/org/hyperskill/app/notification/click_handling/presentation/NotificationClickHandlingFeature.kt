package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.step.domain.model.StepRoute

object NotificationClickHandlingFeature {
    object State

    sealed interface Message {
        data class NotificationClicked(
            val notificationData: PushNotificationData
        ) : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                object TopicRepetition : NavigateTo

                object Home : NavigateTo

                object Profile : NavigateTo

                object StudyPlan : NavigateTo

                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal interface InternalAction : Action {
        data class LogAnalyticEvent(val event: HyperskillAnalyticEvent) : InternalAction
    }
}
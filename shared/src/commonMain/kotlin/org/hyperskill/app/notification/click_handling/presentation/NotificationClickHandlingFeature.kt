package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute

object NotificationClickHandlingFeature {
    object State

    sealed interface Message {
        data class NotificationClicked(
            val notificationData: PushNotificationData
        ) : Message
    }

    internal sealed interface ProfileFetchResult : Message {
        data class Success(val profile: Profile) : ProfileFetchResult
        object Error : ProfileFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {

            data class SetLoadingShowed(val isLoadingShowed: Boolean) : ViewAction

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

        object FetchProfile : InternalAction
    }
}
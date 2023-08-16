package org.hyperskill.app.notification.click_handling.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute

object NotificationClickHandlingFeature {
    object State

    sealed interface Message {

        /**
         * If [isUserAuthorized] == false, then just logs analytics event.
         * Otherwise, also executes navigation to the appropriate for the [notificationData] screen.
         */
        data class NotificationClicked(
            val notificationData: PushNotificationData,
            val isUserAuthorized: Boolean,
            val notificationLaunchedApp: Boolean
        ) : Message

        /**
         * Analytic
         */
        data class EarnedBadgeModalShownEventMessage(val badgeKind: BadgeKind) : Message
        data class EarnedBadgeModalHiddenEventMessage(val badgeKind: BadgeKind) : Message
    }

    internal sealed interface ProfileFetchResult : Message {
        data class Success(val profile: Profile) : ProfileFetchResult
        object Error : ProfileFetchResult
    }

    internal sealed interface EarnedBadgeFetchResult : Message {
        data class Success(val badge: Badge) : EarnedBadgeFetchResult
        object Error : EarnedBadgeFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {

            data class SetLoadingShowed(val isLoadingShowed: Boolean) : ViewAction

            data class ShowEarnedBadgeModal(val badge: Badge) : ViewAction

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

        data class FetchEarnedBadge(val badgeId: Long) : InternalAction
    }
}
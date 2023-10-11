package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingViewedHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.InternalAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class NotificationsOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        state to when (message) {
            Message.AllowNotificationClicked ->
                setOf(
                    InternalAction.LogAnalyticsEvent(
                        NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent
                    ),
                    InternalAction.LogAnalyticsEvent(
                        NotificationSystemNoticeShownHyperskillAnalyticEvent(
                            HyperskillAnalyticRoute.Onboarding.Notifications
                        )
                    ),
                    InternalAction.UpdateLastNotificationPermissionRequestTime,
                    Action.ViewAction.RequestNotificationPermission
                )
            is Message.NotificationPermissionRequestResult ->
                setOf(
                    InternalAction.LogAnalyticsEvent(
                        NotificationSystemNoticeHiddenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.Onboarding.Notifications,
                            isAllowed = message.isPermissionGranted
                        )
                    ),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.RemindMeLaterClicked ->
                setOf(
                    InternalAction.LogAnalyticsEvent(
                        NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent
                    ),
                    InternalAction.UpdateLastNotificationPermissionRequestTime,
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.ViewedEventMessage ->
                setOf(InternalAction.LogAnalyticsEvent(NotificationsOnboardingViewedHyperskillAnalyticsEvent))
        }
}
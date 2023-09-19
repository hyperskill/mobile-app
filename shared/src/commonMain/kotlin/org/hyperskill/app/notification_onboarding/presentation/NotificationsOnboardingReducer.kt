package org.hyperskill.app.notification_onboarding.presentation

import org.hyperskill.app.notification_onboarding.domain.analytics.NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent
import org.hyperskill.app.notification_onboarding.domain.analytics.NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent
import org.hyperskill.app.notification_onboarding.domain.analytics.NotificationsOnboardingPermissionResultHyperskillAnalyticsEvent
import org.hyperskill.app.notification_onboarding.domain.analytics.NotificationsOnboardingViewedHyperskillAnalyticsEvent
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class NotificationsOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        state to when (message) {
            Message.AllowNotificationClicked ->
                setOf(
                    Action.LogAnalyticsEvent(NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent),
                    Action.ViewAction.RequestNotificationPermission
                )
            is Message.NotificationPermissionRequestResult ->
                setOf(
                    Action.LogAnalyticsEvent(
                        NotificationsOnboardingPermissionResultHyperskillAnalyticsEvent(message.isGranted)
                    ),
                    Action.UpdateLastNotificationPermissionRequestTime,
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.RemindMeLaterClicked ->
                setOf(
                    Action.LogAnalyticsEvent(NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.ViewedEventMessage ->
                setOf(Action.LogAnalyticsEvent(NotificationsOnboardingViewedHyperskillAnalyticsEvent))
        }
}
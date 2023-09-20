package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.notifications_onboarding.domain.analytics.NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytics.NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytics.NotificationsOnboardingPermissionResultHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytics.NotificationsOnboardingViewedHyperskillAnalyticsEvent
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
                    InternalAction.LogAnalyticsEvent(NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent),
                    Action.ViewAction.RequestNotificationPermission
                )
            is Message.NotificationPermissionRequestResult ->
                setOf(
                    InternalAction.LogAnalyticsEvent(
                        NotificationsOnboardingPermissionResultHyperskillAnalyticsEvent(message.isGranted)
                    ),
                    InternalAction.UpdateLastNotificationPermissionRequestTime,
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.RemindMeLaterClicked ->
                setOf(
                    InternalAction.LogAnalyticsEvent(NotificationsOnboardingClickedRemindMeLaterHyperskillAnalyticsEvent),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            Message.ViewedEventMessage ->
                setOf(InternalAction.LogAnalyticsEvent(NotificationsOnboardingViewedHyperskillAnalyticsEvent))
        }
}
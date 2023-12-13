package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedDailyStudyRemindsIntervalHourHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedNotNowHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingCompletionAppsFlyerAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingDailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingDailyStudyRemindersIntervalStartHourPickerModalShownEventMessage
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingViewedHyperskillAnalyticsEvent
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.InternalAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class NotificationsOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.AllowNotificationsClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticsEvent(
                            selectedDailyStudyRemindersStartHour = state.dailyStudyRemindersStartHour
                        )
                    ),
                    InternalAction.LogAnalyticEvent(
                        NotificationSystemNoticeShownHyperskillAnalyticEvent(
                            HyperskillAnalyticRoute.Onboarding.Notifications
                        )
                    ),
                    InternalAction.SaveDailyStudyRemindersIntervalStartHour(
                        startHour = state.dailyStudyRemindersStartHour
                    ),
                    Action.ViewAction.RequestNotificationPermission
                )
            }
            is Message.NotificationPermissionRequestResult -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationSystemNoticeHiddenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.Onboarding.Notifications,
                            isAllowed = message.isPermissionGranted
                        )
                    ),
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingCompletionAppsFlyerAnalyticEvent(
                            isSuccess = message.isPermissionGranted
                        )
                    ),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            }
            Message.NotNowClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedNotNowHyperskillAnalyticsEvent
                    ),
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingCompletionAppsFlyerAnalyticEvent(isSuccess = false)
                    ),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            }
            Message.DailyStudyRemindsIntervalHourClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedDailyStudyRemindsIntervalHourHyperskillAnalyticsEvent(
                            currentDailyStudyRemindersStartHour = state.dailyStudyRemindersStartHour
                        )
                    ),
                    Action.ViewAction.ShowDailyStudyRemindersIntervalStartHourPickerModal
                )
            }
            is Message.DailyStudyRemindsIntervalStartHourSelected -> {
                state.copy(dailyStudyRemindersStartHour = message.startHour) to emptySet()
            }
            Message.DailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingDailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage
                    )
                )
            }
            Message.DailyStudyRemindersIntervalStartHourPickerModalShownEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingDailyStudyRemindersIntervalStartHourPickerModalShownEventMessage
                    )
                )
            }
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(NotificationsOnboardingViewedHyperskillAnalyticsEvent))
            }
        }
}
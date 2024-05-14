package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeHiddenHyperskillAnalyticEvent
import org.hyperskill.app.notification.local.domain.analytic.NotificationSystemNoticeShownHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedDailyStudyRemindsIntervalHourHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingClickedNotNowHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingDailyStudyRemindersIntervalPickerModalClickedConfirmHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingDailyStudyRemindersIntervalPickerModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingDailyStudyRemindersIntervalPickerModalShownHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.domain.analytic.NotificationsOnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.InternalAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import org.hyperskill.app.notifications_onboarding.view.mapper.NotificationsOnboardingViewStateMapper
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class NotificationsOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.AllowNotificationsClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedAllowNotificationsHyperskillAnalyticEvent(
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
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            }
            Message.NotNowClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedNotNowHyperskillAnalyticEvent
                    ),
                    Action.ViewAction.CompleteNotificationOnboarding
                )
            }
            Message.DailyStudyRemindsIntervalHourClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingClickedDailyStudyRemindsIntervalHourHyperskillAnalyticEvent(
                            currentDailyStudyRemindersStartHour = state.dailyStudyRemindersStartHour
                        )
                    ),
                    Action.ViewAction.ShowDailyStudyRemindersIntervalStartHourPickerModal(
                        intervals = (0..23).map { hour ->
                            NotificationsOnboardingViewStateMapper.formatDailyStudyRemindersInterval(startHour = hour)
                        },
                        dailyStudyRemindersStartHour = state.dailyStudyRemindersStartHour
                    )
                )
            }
            is Message.DailyStudyRemindsIntervalStartHourSelected -> {
                val analyticEvent =
                    NotificationsOnboardingDailyStudyRemindersIntervalPickerModalClickedConfirmHyperskillAnalyticEvent(
                        selectedDailyStudyRemindersStartHour = message.startHour
                    )
                state.copy(
                    dailyStudyRemindersStartHour = message.startHour
                ) to setOf(InternalAction.LogAnalyticEvent(analyticEvent))
            }
            Message.DailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingDailyStudyRemindersIntervalPickerModalHiddenHyperskillAnalyticEvent
                    )
                )
            }
            Message.DailyStudyRemindersIntervalStartHourPickerModalShownEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        NotificationsOnboardingDailyStudyRemindersIntervalPickerModalShownHyperskillAnalyticEvent
                    )
                )
            }
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(NotificationsOnboardingViewedHyperskillAnalyticEvent))
            }
        }
}
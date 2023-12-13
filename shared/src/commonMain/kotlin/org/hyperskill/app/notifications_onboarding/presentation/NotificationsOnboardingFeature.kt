package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.notification.local.cache.NotificationCacheKeyValues

object NotificationsOnboardingFeature {
    data class State(val dailyStudyRemindersStartHour: Int)

    internal fun initialState() =
        State(dailyStudyRemindersStartHour = NotificationCacheKeyValues.DAILY_STUDY_REMINDERS_START_HOUR_ONBOARDING)

    sealed interface Message {
        object AllowNotificationsClicked : Message
        object NotNowClicked : Message
        data class NotificationPermissionRequestResult(val isPermissionGranted: Boolean) : Message

        object DailyStudyRemindsIntervalHourClicked : Message
        data class DailyStudyRemindsIntervalStartHourSelected(val startHour: Int) : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message

        object DailyStudyRemindersIntervalStartHourPickerModalShownEventMessage : Message
        object DailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object RequestNotificationPermission : ViewAction
            object CompleteNotificationOnboarding : ViewAction

            object ShowDailyStudyRemindersIntervalStartHourPickerModal : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class SaveDailyStudyRemindersIntervalStartHour(val startHour: Int) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
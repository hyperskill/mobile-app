package org.hyperskill.app.notifications_onboarding.presentation

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object NotificationsOnboardingFeature {
    internal data class State(val dailyStudyRemindersStartHour: Int)

    data class ViewState(val formattedDailyStudyRemindersInterval: String)

    internal fun initialState() =
        State(
            dailyStudyRemindersStartHour = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .hour
        )

    sealed interface Message {
        data object AllowNotificationsClicked : Message
        data object NotNowClicked : Message
        data class NotificationPermissionRequestResult(val isPermissionGranted: Boolean) : Message

        data object DailyStudyRemindsIntervalHourClicked : Message
        data class DailyStudyRemindsIntervalStartHourSelected(val startHour: Int) : Message

        /**
         * Analytic
         */
        data object ViewedEventMessage : Message

        data object DailyStudyRemindersIntervalStartHourPickerModalShownEventMessage : Message
        data object DailyStudyRemindersIntervalStartHourPickerModalHiddenEventMessage : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data object RequestNotificationPermission : ViewAction
            data object CompleteNotificationOnboarding : ViewAction

            data class ShowDailyStudyRemindersIntervalStartHourPickerModal(
                val intervals: List<String>,
                val dailyStudyRemindersStartHour: Int
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class SaveDailyStudyRemindersIntervalStartHour(val startHour: Int) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
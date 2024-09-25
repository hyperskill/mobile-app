package org.hyperskill.app.notification_daily_study_reminder_widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object NotificationDailyStudyReminderWidgetFeature {
    sealed interface State {
        data object Idle : State
        data object Loading : State
        data object Hidden : State
        data class Data(val passedTopicsCount: Int) : State
    }

    sealed interface ViewState {
        data object Hidden : ViewState
        data object Visible : ViewState
    }

    sealed interface Message {
        data class Initialize(
            val isNotificationPermissionGranted: Boolean
        ) : Message

        data object CloseClicked : Message
        data object WidgetClicked : Message

        data object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class FetchWidgetDataResult(
            val passedTopicsCount: Int,
            val isWidgetHidden: Boolean
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data object FetchWidgetData : InternalAction

        data object HideWidget : InternalAction

        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction
    }
}
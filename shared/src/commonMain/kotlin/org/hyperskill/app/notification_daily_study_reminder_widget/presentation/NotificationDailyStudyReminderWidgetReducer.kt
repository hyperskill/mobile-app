package org.hyperskill.app.notification_daily_study_reminder_widget.presentation

import org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic.NotificationDailyStudyReminderWidgetClickedCloseHyperskillAnalyticEvent
import org.hyperskill.app.notification_daily_study_reminder_widget.domain.analytic.NotificationDailyStudyReminderWidgetViewedHyperskillAnalyticEvent
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Action
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalAction
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.InternalMessage
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.Message
import org.hyperskill.app.notification_daily_study_reminder_widget.presentation.NotificationDailyStudyReminderWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class NotificationDailyStudyReminderWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.Initialize -> handleInitialize(state, message)
            is InternalMessage.FetchWidgetDataResult -> handleFetchWidgetDataResult(state, message)
            Message.CloseClicked -> handleCloseClicked(state)
            Message.WidgetClicked -> handleWidgetClicked(state)
            is InternalMessage.PassedTopicsCountChanged -> handlePassedTopicsCountChanged(state, message)
            Message.ViewedEventMessage -> handleViewedEvent(state)
        } ?: (state to emptySet())

    private fun handleInitialize(
        state: State,
        message: Message.Initialize
    ): ReducerResult? =
        if (state is State.Idle && !message.isNotificationPermissionGranted) {
            State.Loading to setOf(InternalAction.FetchWidgetData)
        } else {
            null
        }

    private fun handleFetchWidgetDataResult(
        state: State,
        message: InternalMessage.FetchWidgetDataResult
    ): ReducerResult? {
        if (state !is State.Loading) {
            return null
        }

        return if (message.isWidgetHidden) {
            State.Hidden to emptySet()
        } else {
            State.Data(passedTopicsCount = message.passedTopicsCount) to emptySet()
        }
    }

    private fun handleCloseClicked(state: State): ReducerResult? =
        if (state is State.Data) {
            State.Hidden to setOf(
                InternalAction.HideWidget,
                InternalAction.LogAnalyticEvent(
                    NotificationDailyStudyReminderWidgetClickedCloseHyperskillAnalyticEvent
                )
            )
        } else {
            null
        }

    private fun handleWidgetClicked(state: State): ReducerResult? =
        if (state is State.Data) {
            state to emptySet()
        } else {
            null
        }

    private fun handlePassedTopicsCountChanged(
        state: State,
        message: InternalMessage.PassedTopicsCountChanged
    ): ReducerResult? =
        if (state is State.Data) {
            state.copy(passedTopicsCount = message.passedTopicsCount) to emptySet()
        } else {
            null
        }

    private fun handleViewedEvent(state: State): ReducerResult? =
        if (state is State.Data) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    NotificationDailyStudyReminderWidgetViewedHyperskillAnalyticEvent
                )
            )
        } else {
            null
        }
}
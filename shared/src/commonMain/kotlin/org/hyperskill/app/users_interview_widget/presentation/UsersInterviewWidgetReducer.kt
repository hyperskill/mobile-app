package org.hyperskill.app.users_interview_widget.presentation

import org.hyperskill.app.users_interview_widget.domain.analytic.UsersInterviewWidgetClickedCloseHyperskillAnalyticEvent
import org.hyperskill.app.users_interview_widget.domain.analytic.UsersInterviewWidgetClickedHyperskillAnalyticEvent
import org.hyperskill.app.users_interview_widget.domain.analytic.UsersInterviewWidgetViewedHyperskillAnalyticEvent
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Action
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalAction
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.InternalMessage
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Message
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class UsersInterviewWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            InternalMessage.Initialize ->
                if (state is State.Idle) {
                    State.Loading to setOf(InternalAction.FetchUsersInterviewWidgetData)
                } else {
                    null
                }
            is InternalMessage.FetchUsersInterviewWidgetDataResult ->
                if (message.isUsersInterviewWidgetEnabled && !message.isUsersInterviewWidgetHidden) {
                    State.Visible to emptySet()
                } else {
                    State.Hidden to emptySet()
                }
            is InternalMessage.UsersInterviewWidgetFeatureFlagChanged ->
                if (state is State.Visible && !message.isUsersInterviewWidgetEnabled) {
                    State.Hidden to emptySet()
                } else {
                    null
                }
            Message.CloseClicked ->
                if (state is State.Visible) {
                    State.Hidden to setOf(
                        InternalAction.HideUsersInterviewWidget,
                        InternalAction.LogAnalyticEvent(UsersInterviewWidgetClickedCloseHyperskillAnalyticEvent)
                    )
                } else {
                    null
                }
            Message.WidgetClicked ->
                if (state is State.Visible) {
                    State.Hidden to setOf(
                        InternalAction.FetchUsersInterviewUrl,
                        InternalAction.HideUsersInterviewWidget,
                        InternalAction.LogAnalyticEvent(UsersInterviewWidgetClickedHyperskillAnalyticEvent)
                    )
                } else {
                    null
                }
            is InternalMessage.FetchUsersInterviewUrlResult ->
                state to setOf(Action.ViewAction.ShowUsersInterview(url = message.url))
            Message.ViewedEventMessage ->
                if (state is State.Visible) {
                    state to setOf(
                        InternalAction.LogAnalyticEvent(UsersInterviewWidgetViewedHyperskillAnalyticEvent)
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
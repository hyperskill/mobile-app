package org.hyperskill.app.users_interview_widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object UsersInterviewWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Hidden : State
        object Visible : State
    }

    sealed interface Message {
        object CloseClicked : Message
        object WidgetClicked : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        object Initialize : InternalMessage

        data class FetchUsersInterviewWidgetDataResult(
            val isUsersInterviewWidgetEnabled: Boolean,
            val isUsersInterviewWidgetHidden: Boolean
        ) : InternalMessage

        data class UsersInterviewWidgetFeatureFlagChanged(
            val isUsersInterviewWidgetEnabled: Boolean
        ) : InternalMessage

        data class FetchUsersInterviewUrlResult(
            val url: String
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowUsersInterview(val url: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchUsersInterviewWidgetData : InternalAction

        object FetchUsersInterviewUrl : InternalAction

        object HideUsersInterviewWidget : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
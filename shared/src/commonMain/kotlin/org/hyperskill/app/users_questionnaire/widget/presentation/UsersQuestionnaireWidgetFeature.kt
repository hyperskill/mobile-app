package org.hyperskill.app.users_questionnaire.widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object UsersQuestionnaireWidgetFeature {
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

        data class FetchUsersQuestionnaireWidgetDataResult(
            val isUsersQuestionnaireEnabled: Boolean,
            val isUsersQuestionnaireWidgetHidden: Boolean
        ) : InternalMessage

        data class UsersQuestionnaireFeatureFlagChanged(
            val isUsersQuestionnaireEnabled: Boolean
        ) : InternalMessage

        data class FetchUsersQuestionnaireUrlResult(
            val url: String
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowUsersQuestionnaire(val url: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchUsersQuestionnaireWidgetData : InternalAction

        object FetchUsersQuestionnaireUrl : InternalAction

        object HideUsersQuestionnaireWidget : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
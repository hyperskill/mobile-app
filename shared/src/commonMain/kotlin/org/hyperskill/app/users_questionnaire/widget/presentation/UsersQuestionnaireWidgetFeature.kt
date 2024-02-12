package org.hyperskill.app.users_questionnaire.widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

object UsersQuestionnaireWidgetFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Hidden : State
        object Visible : State
    }

    sealed interface Message

    internal sealed interface InternalMessage : Message {
        object Initialize : InternalMessage

        data class FetchUsersQuestionnaireWidgetDataResult(
            val isUsersQuestionnaireEnabled: Boolean,
            val isUsersQuestionnaireWidgetHidden: Boolean
        ) : InternalMessage

        data class UsersQuestionnaireFeatureFlagChanged(
            val isUsersQuestionnaireEnabled: Boolean
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        object FetchUsersQuestionnaireWidgetData : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
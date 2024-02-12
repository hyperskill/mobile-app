package org.hyperskill.app.users_questionnaire.widget.presentation

import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.Action
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.InternalAction
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.InternalMessage
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.Message
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class UsersQuestionnaireWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            InternalMessage.Initialize -> {
                if (state is State.Idle) {
                    State.Loading to setOf(InternalAction.FetchUsersQuestionnaireWidgetData)
                } else {
                    null
                }
            }
            is InternalMessage.FetchUsersQuestionnaireWidgetDataResult ->
                if (message.isUsersQuestionnaireEnabled && !message.isUsersQuestionnaireWidgetHidden) {
                    State.Visible to emptySet()
                } else {
                    State.Hidden to emptySet()
                }
            is InternalMessage.UsersQuestionnaireFeatureFlagChanged ->
                if (state is State.Visible && !message.isUsersQuestionnaireEnabled) {
                    State.Hidden to emptySet()
                } else {
                    null
                }
        } ?: (state to emptySet())
}
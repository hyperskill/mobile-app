package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserClickedSignInHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.domain.analytic.PlaceholderNewUserViewedHyperskillAnalyticEvent
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Action
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.Message
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class PlaceholderNewUserReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.PlaceholderSignInTappedMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(PlaceholderNewUserClickedSignInHyperskillAnalyticEvent()),
                    Action.Logout
                )
            is Message.PlaceholderViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(PlaceholderNewUserViewedHyperskillAnalyticEvent()))
            is Message.PlaceholderClickedContinueEventMessage ->
                state to setOf(Action.LogAnalyticEvent(PlaceholderNewUserClickedContinueHyperskillAnalyticEvent()))
        }
}

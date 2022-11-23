package org.hyperskill.app.placeholder_new_user.presentation

import org.hyperskill.app.core.domain.url.HyperskillUrlPath
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
            is Message.OpenAuthScreen ->
                state to setOf(Action.ViewAction.NavigateTo.AuthScreen)
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(PlaceholderNewUserViewedHyperskillAnalyticEvent()))
            is Message.ClickedContinueEventMessage ->
                state to setOf(Action.LogAnalyticEvent(PlaceholderNewUserClickedContinueHyperskillAnalyticEvent()))
            Message.ClickedContinueOnWeb -> {
                if (state is State.Content) {
                    state.copy(isLinkLoadingShown = true) to setOf(Action.GetLink(HyperskillUrlPath.Index()))
                } else {
                    null
                }
            }
            is Message.LinkReceived -> {
                if (state is State.Content) {
                    state.copy(isLinkLoadingShown = false) to setOf(Action.ViewAction.FollowUrl(message.url))
                } else {
                    null
                }
            }
            Message.LinkReceiveFailed -> {
                if (state is State.Content) {
                    //TODO: implement error showing
                    state.copy(isLinkLoadingShown = false) to emptySet()
                } else {
                    null
                }
            }
        } ?: ( state to emptySet() )
}

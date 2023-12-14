package org.hyperskill.app.welcome.presentation

import org.hyperskill.app.welcome.domain.analytic.WelcomeScreenClickedSignInHyperskillAnalyticEvent
import org.hyperskill.app.welcome.domain.analytic.WelcomeScreenClickedSignUnHyperskillAnalyticEvent
import org.hyperskill.app.welcome.domain.analytic.WelcomeScreenViewedHyperskillAnalyticEvent
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Action
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Message
import org.hyperskill.app.welcome.presentation.WelcomeFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal class WelcomeReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchProfile)
                } else {
                    null
                }
            is Message.ProfileFetchSuccess ->
                if (state is State.Loading) {
                    State.Content(isAuthorized = !message.profile.isGuest) to emptySet()
                } else {
                    null
                }
            is Message.ProfileFetchFailure ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.ClickedSignUn ->
                if (state is State.Content) {
                    val navigateToViewAction = if (state.isAuthorized) {
                        Action.ViewAction.NavigateTo.TrackSelectionListScreen
                    } else {
                        Action.ViewAction.NavigateTo.AuthScreen(isInSignUpMode = true)
                    }

                    state to setOf(
                        Action.LogAnalyticEvent(WelcomeScreenClickedSignUnHyperskillAnalyticEvent),
                        navigateToViewAction
                    )
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(WelcomeScreenViewedHyperskillAnalyticEvent))
            is Message.ClickedSignInEventMessage ->
                state to setOf(Action.LogAnalyticEvent(WelcomeScreenClickedSignInHyperskillAnalyticEvent))
        } ?: (state to emptySet())
}
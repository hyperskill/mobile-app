package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.onboarding.domain.analytic.OnboardingClickedSignInHyperskillAnalyticEvent
import org.hyperskill.app.onboarding.domain.analytic.OnboardingClickedSignUnHyperskillAnalyticEvent
import org.hyperskill.app.onboarding.domain.analytic.OnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class OnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchOnboarding)
                } else {
                    null
                }
            is Message.OnboardingSuccess ->
                if (state is State.Loading) {
                    State.Content(isAuthorized = !message.profile.isGuest) to emptySet()
                } else {
                    null
                }
            is Message.OnboardingFailure ->
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
                        Action.LogAnalyticEvent(OnboardingClickedSignUnHyperskillAnalyticEvent()),
                        navigateToViewAction
                    )
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(OnboardingViewedHyperskillAnalyticEvent()))
            is Message.ClickedSignInEventMessage ->
                state to setOf(Action.LogAnalyticEvent(OnboardingClickedSignInHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}
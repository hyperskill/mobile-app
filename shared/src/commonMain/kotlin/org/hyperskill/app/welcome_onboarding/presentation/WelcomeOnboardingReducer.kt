package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias WelcomeOnboardingReducerResult = Pair<State, Set<Action>>

internal class WelcomeOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): WelcomeOnboardingReducerResult =
        when (message) {
            else -> {
                state to emptySet()
            }
        }
}
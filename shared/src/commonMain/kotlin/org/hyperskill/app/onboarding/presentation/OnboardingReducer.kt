package org.hyperskill.app.onboarding.presentation

import ru.nobird.app.presentation.redux.reducer.StateReducer
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.State
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message

class OnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                state to setOf(Action.FetchOnboarding)
        }
}
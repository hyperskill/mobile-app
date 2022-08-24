package org.hyperskill.app.onboarding.presentation

import org.hyperskill.app.onboarding.domain.analytic.OnboardingViewedHyperskillAnalyticEvent
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Action
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.Message
import org.hyperskill.app.onboarding.presentation.OnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class OnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Init ->
                state to setOf(Action.FetchOnboarding)
            is Message.OnboardingViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(OnboardingViewedHyperskillAnalyticEvent()))
        }
}
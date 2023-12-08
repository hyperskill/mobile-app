package org.hyperskill.app.search.presentation

import org.hyperskill.app.search.domain.analytic.SearchViewedHyperskillAnalyticEvent
import org.hyperskill.app.search.presentation.SearchFeature.Action
import org.hyperskill.app.search.presentation.SearchFeature.InternalAction
import org.hyperskill.app.search.presentation.SearchFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias SearchReducerResult = Pair<State, Set<Action>>

internal class SearchReducer : StateReducer<State, SearchFeature.Message, Action> {
    override fun reduce(state: State, message: SearchFeature.Message): SearchReducerResult =
        when (message) {
            SearchFeature.Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(SearchViewedHyperskillAnalyticEvent))
            }
        }
}
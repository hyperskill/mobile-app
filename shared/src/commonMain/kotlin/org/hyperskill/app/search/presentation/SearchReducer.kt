package org.hyperskill.app.search.presentation

import org.hyperskill.app.search.domain.analytic.SearchClickedSearchHyperskillAnalyticEvent
import org.hyperskill.app.search.domain.analytic.SearchViewedHyperskillAnalyticEvent
import org.hyperskill.app.search.presentation.SearchFeature.Action
import org.hyperskill.app.search.presentation.SearchFeature.InternalAction
import org.hyperskill.app.search.presentation.SearchFeature.InternalMessage
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.search.presentation.SearchFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias SearchReducerResult = Pair<State, Set<Action>>

internal class SearchReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): SearchReducerResult =
        when (message) {
            is Message.QueryChanged -> {
                handleQueryChangedMessage(state, message)
            }
            Message.SearchClicked -> {
                handleSearchClickedMessage(state)
            }
            InternalMessage.PerformSearchError -> {
                if (state.searchResultsState == SearchFeature.SearchResultsState.Loading) {
                    state.copy(
                        searchResultsState = SearchFeature.SearchResultsState.Error
                    ) to emptySet()
                } else {
                    null
                }
            }
            is InternalMessage.PerformSearchSuccess -> {
                if (state.searchResultsState == SearchFeature.SearchResultsState.Loading) {
                    state.copy(
                        searchResultsState = SearchFeature.SearchResultsState.Content(message.topics)
                    ) to emptySet()
                } else {
                    null
                }
            }
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(SearchViewedHyperskillAnalyticEvent))
            }
        } ?: (state to emptySet())

    private fun handleQueryChangedMessage(
        state: State,
        message: Message.QueryChanged
    ): SearchReducerResult? {
        if (state.searchResultsState == SearchFeature.SearchResultsState.Loading) {
            return null
        }

        val newSearchResultsState = when (state.searchResultsState) {
            SearchFeature.SearchResultsState.Editing -> SearchFeature.SearchResultsState.Editing
            SearchFeature.SearchResultsState.Loading -> SearchFeature.SearchResultsState.Loading
            SearchFeature.SearchResultsState.Error -> SearchFeature.SearchResultsState.Editing
            is SearchFeature.SearchResultsState.Content -> SearchFeature.SearchResultsState.Editing
        }

        return state.copy(
            query = message.query,
            searchResultsState = newSearchResultsState
        ) to emptySet()
    }

    private fun handleSearchClickedMessage(state: State): SearchReducerResult {
        val analyticActions = setOf(
            InternalAction.LogAnalyticEvent(SearchClickedSearchHyperskillAnalyticEvent(state.query))
        )

        if (state.query.isBlank()) {
            return state to analyticActions
        }

        return when (state.searchResultsState) {
            SearchFeature.SearchResultsState.Editing,
            SearchFeature.SearchResultsState.Error ->
                state to (analyticActions + InternalAction.PerformSearch(state.query))
            SearchFeature.SearchResultsState.Loading,
            is SearchFeature.SearchResultsState.Content ->
                state to analyticActions
        }
    }
}
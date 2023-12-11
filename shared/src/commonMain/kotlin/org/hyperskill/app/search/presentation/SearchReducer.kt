package org.hyperskill.app.search.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.search.domain.analytic.SearchClickedItemHyperskillAnalyticEvent
import org.hyperskill.app.search.domain.analytic.SearchClickedRetrySearchHyperskillAnalyticEvent
import org.hyperskill.app.search.domain.analytic.SearchClickedSearchHyperskillAnalyticEvent
import org.hyperskill.app.search.domain.analytic.SearchViewedHyperskillAnalyticEvent
import org.hyperskill.app.search.presentation.SearchFeature.Action
import org.hyperskill.app.search.presentation.SearchFeature.InternalAction
import org.hyperskill.app.search.presentation.SearchFeature.InternalMessage
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.search.presentation.SearchFeature.State
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias SearchReducerResult = Pair<State, Set<Action>>

internal class SearchReducer(
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): SearchReducerResult =
        when (message) {
            is Message.QueryChanged -> {
                handleQueryChangedMessage(state, message)
            }
            Message.SearchClicked -> {
                handleSearchClickedMessage(state)
            }
            Message.RetrySearchClicked -> {
                if (state.searchResultsState == SearchFeature.SearchResultsState.Error) {
                    state.copy(
                        searchResultsState = SearchFeature.SearchResultsState.Loading
                    ) to setOf(
                        InternalAction.PerformSearch(query = state.query, withDelay = false),
                        InternalAction.LogAnalyticEvent(SearchClickedRetrySearchHyperskillAnalyticEvent)
                    )
                } else {
                    null
                }
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
            is Message.SearchResultsItemClicked -> {
                handleSearchResultsItemClickedMessage(state, message)
            }
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(SearchViewedHyperskillAnalyticEvent))
            }
        } ?: (state to emptySet())

    private fun handleQueryChangedMessage(
        state: State,
        message: Message.QueryChanged
    ): SearchReducerResult =
        if (message.query.isBlank()) {
            state.copy(
                query = message.query,
                searchResultsState = SearchFeature.SearchResultsState.Idle
            ) to setOf(InternalAction.CancelSearch)
        } else {
            state.copy(
                query = message.query,
                searchResultsState = SearchFeature.SearchResultsState.Loading
            ) to setOf(InternalAction.PerformSearch(query = message.query, withDelay = true))
        }

    private fun handleSearchClickedMessage(state: State): SearchReducerResult {
        val analyticActions = setOf(
            InternalAction.LogAnalyticEvent(SearchClickedSearchHyperskillAnalyticEvent(state.query))
        )

        if (state.query.isBlank()) {
            return state to analyticActions
        }

        return when (state.searchResultsState) {
            SearchFeature.SearchResultsState.Error ->
                state.copy(
                    searchResultsState = SearchFeature.SearchResultsState.Loading
                ) to (analyticActions + InternalAction.PerformSearch(query = state.query, withDelay = false))
            SearchFeature.SearchResultsState.Idle,
            SearchFeature.SearchResultsState.Loading,
            is SearchFeature.SearchResultsState.Content ->
                state to analyticActions
        }
    }

    private fun handleSearchResultsItemClickedMessage(
        state: State,
        message: Message.SearchResultsItemClicked
    ): SearchReducerResult? {
        if (state.searchResultsState is SearchFeature.SearchResultsState.Content) {
            val targetTopic = state.searchResultsState.topics.firstOrNull { it.id == message.id } ?: return null
            return state to buildSet {
                if (targetTopic.theoryId != null) {
                    add(
                        Action.ViewAction.OpenStepScreen(
                            StepRoute.Learn.TheoryOpenedFromSearch(targetTopic.theoryId)
                        )
                    )
                } else {
                    add(
                        Action.ViewAction.OpenStepScreenFailed(
                            resourceProvider.getString(SharedResources.strings.search_open_step_screen_error_message)
                        )
                    )
                }
                add(
                    InternalAction.LogAnalyticEvent(
                        SearchClickedItemHyperskillAnalyticEvent(query = state.query, topicId = targetTopic.id)
                    )
                )
            }
        } else {
            return null
        }
    }
}
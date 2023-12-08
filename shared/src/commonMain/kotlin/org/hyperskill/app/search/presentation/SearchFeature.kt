package org.hyperskill.app.search.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.topics.domain.model.Topic

object SearchFeature {
    internal data class State(
        val query: String,
        val searchResultsState: SearchResultsState
    )

    internal sealed interface SearchResultsState {
        object Editing : SearchResultsState
        object Loading : SearchResultsState
        object Error : SearchResultsState
        data class Content(val topics: List<Topic>) : SearchResultsState
    }

    data class ViewState(
        val query: String
    )

    internal fun initialState(): State =
        State(
            query = "",
            searchResultsState = SearchResultsState.Editing
        )

    sealed interface Message {
        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message

    sealed interface Action {
        sealed interface ViewAction : Action
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
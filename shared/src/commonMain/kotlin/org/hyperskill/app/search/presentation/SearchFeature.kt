package org.hyperskill.app.search.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topics.domain.model.Topic
import ru.nobird.app.core.model.Identifiable

object SearchFeature {
    internal data class State(
        val query: String,
        val searchResultsState: SearchResultsState
    )

    internal sealed interface SearchResultsState {
        object Idle : SearchResultsState
        object Loading : SearchResultsState
        object Error : SearchResultsState
        data class Content(val topics: List<Topic>) : SearchResultsState
    }

    data class ViewState(
        val query: String,
        val searchResultsViewState: SearchResultsViewState
    )

    sealed interface SearchResultsViewState {
        object Idle : SearchResultsViewState
        object Loading : SearchResultsViewState
        object Error : SearchResultsViewState
        object Empty : SearchResultsViewState
        data class Content(val searchResults: List<Item>) : SearchResultsViewState {
            data class Item(
                override val id: Long,
                val title: String
            ) : Identifiable<Long>
        }
    }

    internal fun initialState(): State =
        State(
            query = "",
            searchResultsState = SearchResultsState.Idle
        )

    sealed interface Message {
        data class QueryChanged(val query: String) : Message

        object SearchClicked : Message
        object RetrySearchClicked : Message

        data class SearchResultsItemClicked(val id: Long) : Message

        object ViewedEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class PerformSearchError(val error: Throwable) : InternalMessage
        data class PerformSearchSuccess(val topics: List<Topic>) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class OpenStepScreen(val stepRoute: StepRoute) : ViewAction
            data class OpenStepScreenFailed(val message: String) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class PerformSearch(val query: String, val withDelay: Boolean) : InternalAction
        object CancelSearch : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
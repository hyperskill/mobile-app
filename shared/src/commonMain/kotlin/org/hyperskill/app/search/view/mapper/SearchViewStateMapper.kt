package org.hyperskill.app.search.view.mapper

import org.hyperskill.app.search.presentation.SearchFeature

internal object SearchViewStateMapper {
    fun map(state: SearchFeature.State): SearchFeature.ViewState =
        SearchFeature.ViewState(
            query = state.query,
            searchResultsViewState = mapSearchResultsState(state.searchResultsState)
        )

    private fun mapSearchResultsState(
        state: SearchFeature.SearchResultsState
    ): SearchFeature.SearchResultsViewState =
        when (state) {
            SearchFeature.SearchResultsState.Idle -> SearchFeature.SearchResultsViewState.Idle
            SearchFeature.SearchResultsState.Loading -> SearchFeature.SearchResultsViewState.Loading
            SearchFeature.SearchResultsState.Error -> SearchFeature.SearchResultsViewState.Error
            is SearchFeature.SearchResultsState.Content -> {
                if (state.topics.isEmpty()) {
                    SearchFeature.SearchResultsViewState.Empty
                } else {
                    SearchFeature.SearchResultsViewState.Content(
                        searchResults = state.topics.map { topic ->
                            SearchFeature.SearchResultsViewState.Content.Item(
                                id = topic.id,
                                title = topic.title
                            )
                        }
                    )
                }
            }
        }
}
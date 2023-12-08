package org.hyperskill.app.search.view.mapper

import org.hyperskill.app.search.presentation.SearchFeature

internal object SearchViewStateMapper {
    fun map(state: SearchFeature.State): SearchFeature.ViewState {
        val searchResultsViewState = mapSearchResultsState(state.searchResultsState)

        val isSearchButtonEnabled = state.query.isNotBlank() &&
            searchResultsViewState != SearchFeature.SearchResultsViewState.Loading

        return SearchFeature.ViewState(
            query = state.query,
            searchResultsViewState = searchResultsViewState,
            isSearchButtonEnabled = isSearchButtonEnabled,
            isUserInteractionEnabled = searchResultsViewState != SearchFeature.SearchResultsViewState.Loading
        )
    }

    private fun mapSearchResultsState(
        state: SearchFeature.SearchResultsState
    ): SearchFeature.SearchResultsViewState =
        when (state) {
            SearchFeature.SearchResultsState.Editing -> SearchFeature.SearchResultsViewState.Editing
            SearchFeature.SearchResultsState.Loading -> SearchFeature.SearchResultsViewState.Loading
            SearchFeature.SearchResultsState.Error -> SearchFeature.SearchResultsViewState.Error
            is SearchFeature.SearchResultsState.Content ->
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
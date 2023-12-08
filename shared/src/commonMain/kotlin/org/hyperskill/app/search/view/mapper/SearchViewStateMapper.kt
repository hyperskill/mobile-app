package org.hyperskill.app.search.view.mapper

import org.hyperskill.app.search.presentation.SearchFeature

internal object SearchViewStateMapper {
    fun map(state: SearchFeature.State): SearchFeature.ViewState =
        SearchFeature.ViewState(
            query = state.query
        )
}
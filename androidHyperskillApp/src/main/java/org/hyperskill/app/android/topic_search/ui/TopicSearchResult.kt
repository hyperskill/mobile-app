package org.hyperskill.app.android.topic_search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.map
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.search.presentation.SearchFeature.Message
import org.hyperskill.app.search.presentation.SearchFeature.SearchResultsViewState
import org.hyperskill.app.search.presentation.SearchViewModel

@Composable
fun TopicSearchResult(viewModel: SearchViewModel) {
    val viewState: SearchResultsViewState by viewModel
        .state
        .map { it.searchResultsViewState }
        .collectAsStateWithLifecycle(
            initialValue = SearchResultsViewState.Idle,
            lifecycle = LocalLifecycleOwner.current.lifecycle
        )
    TopicSearchResult(
        viewState = viewState,
        onNewMessage = viewModel::onNewMessage
    )
}

@Composable
fun TopicSearchResult(
    viewState: SearchResultsViewState,
    onNewMessage: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    val insetsPadding = WindowInsets.navigationBars.union(WindowInsets.ime).asPaddingValues()
    Box(modifier = modifier.fillMaxSize()) {
        when (viewState) {
            SearchResultsViewState.Idle -> {
                // no op
            }
            SearchResultsViewState.Empty -> {
                TopicSearchEmptyResult(
                    modifier = Modifier.fillMaxSize()
                )
            }
            SearchResultsViewState.Loading -> {
                TopicSearchSkeleton(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = insetsPadding
                )
            }
            SearchResultsViewState.Error -> {
                ScreenDataLoadingError(
                    errorMessage = stringResource(id = R.string.search_placeholder_error_description)
                ) {
                    onNewMessage(Message.RetrySearchClicked)
                }
            }
            is SearchResultsViewState.Content -> {
                val onItemClick = remember {
                    { id: Long ->
                        onNewMessage(
                            Message.SearchResultsItemClicked(id)
                        )
                    }
                }
                TopicSearchResultContent(
                    items = viewState.searchResults,
                    onItemClick = onItemClick,
                    contentPadding = insetsPadding
                )
            }
        }
    }
}
package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.CenteredHyperskillTopAppBar
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.comments.presentation.CommentsViewModel
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState.DiscussionsViewState

@Composable
fun Comments(
    viewModel: CommentsViewModel,
    onCloseClick: () -> Unit
) {
    val viewState: CommentsScreenViewState by viewModel.state.collectAsStateWithLifecycle()
    Comments(
        viewState,
        onCloseClick = onCloseClick,
        onRetryClick = viewModel::onRetryClick
    )
}

@Composable
fun Comments(
    viewState: CommentsScreenViewState,
    onCloseClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        CenteredHyperskillTopAppBar(
            title = viewState.navigationTitle,
            onNavigationIconClick = onCloseClick
        )
        Box(modifier = Modifier.weight(1f)) {
            when (viewState.discussions) {
                DiscussionsViewState.Idle -> {
                    // no op
                }
                DiscussionsViewState.Loading -> {
                    // TODO
                }
                DiscussionsViewState.Error -> {
                    ScreenDataLoadingError(
                        errorMessage = stringResource(id = R.string.comments_loading_error),
                        onRetryClick = onRetryClick
                    )
                }
                is DiscussionsViewState.Content -> {
                    // TODO
                }
            }
        }
    }
}

private class CommentDiscussionsViewStatePreviewProvider : CollectionPreviewParameterProvider<DiscussionsViewState>(
    listOf(
        DiscussionsViewState.Idle,
        DiscussionsViewState.Loading,
        DiscussionsViewState.Error,
        DiscussionsViewState.Content(
            discussions = emptyList(),
            hasNextPage = false,
            isLoadingNextPage = false
        )
    )
)

@Preview
@Composable
private fun CommentsPreview(
    @PreviewParameter(CommentDiscussionsViewStatePreviewProvider::class) discussionsViewState: DiscussionsViewState
) {
    HyperskillTheme {
        Comments(
            viewState = CommentsScreenViewState(
                navigationTitle = "Comments (34)",
                discussions = discussionsViewState
            ),
            onCloseClick = {},
            onRetryClick = {}
        )
    }
}
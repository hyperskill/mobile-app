package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.leaderboard.presentation.LeaderboardViewModel
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.ListViewState
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel) {
    val viewState: LeaderboardScreenFeature.ViewState by viewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(viewModel) {
        viewModel.onNewMessage(Message.ViewedEventMessage)
        onDispose {
            // no op
        }
    }
    LeaderboardScreen(
        currentTab = viewState.currentTab,
        listState = viewState.listViewState,
        isRefreshing = viewState.isRefreshing,
        onNewMessage = viewModel::onNewMessage
    )
}

@Composable
fun LeaderboardScreen(
    currentTab: LeaderboardScreenFeature.Tab,
    listState: ListViewState,
    isRefreshing: Boolean,
    onNewMessage: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnNewMessage by rememberUpdatedState(newValue = onNewMessage)
    Column(modifier = modifier) {
        LeaderboardTabs(currentTab) { clickedTab ->
            currentOnNewMessage(Message.TabClicked(clickedTab))
        }
        when (listState) {
            ListViewState.Idle -> {
                // no op
            }
            ListViewState.Empty -> {
                LeaderboardStub(
                    modifier = Modifier.weight(1f)
                )
            }
            ListViewState.Loading -> {
                LeaderboardSkeleton()
            }
            ListViewState.Error -> {
                ScreenDataLoadingError(
                    errorMessage = stringResource(id = R.string.leaderboard_placeholder_error_description)
                ) {
                    onNewMessage(Message.RetryContentLoading)
                }
            }
            is ListViewState.Content -> {
                RefreshableLeaderboardList(
                    items = listState.list,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        currentOnNewMessage(Message.PullToRefresh)
                    },
                    onItemClick = { userId ->
                        currentOnNewMessage(Message.ListItemClicked(userId))
                    }
                )
            }
        }
    }

}

private class LeaderboardScreenPreviewProvider : PreviewParameterProvider<ListViewState> {
    override val values: Sequence<ListViewState>
        get() = sequenceOf(
            ListViewState.Idle,
            ListViewState.Empty,
            ListViewState.Error,
            ListViewState.Loading,
            ListViewState.Content(list = LeaderboardPreviewData.listData)
        )
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardScreenPreview(
    @PreviewParameter(LeaderboardScreenPreviewProvider::class) viewState: ListViewState
) {
    HyperskillTheme {
        LeaderboardScreen(
            currentTab = LeaderboardScreenFeature.Tab.DAY,
            listState = viewState,
            isRefreshing = false,
            onNewMessage = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
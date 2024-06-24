package org.hyperskill.app.android.progress.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.android.core.extensions.compose.plus
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillPullRefreshIndicator
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.android.progress.ui.project.ProjectProgress
import org.hyperskill.app.android.progress.ui.track.TrackProgress
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState

@Composable
fun ProgressScreen(
    viewModel: ProgressScreenViewModel,
    onBackClick: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    ProgressScreen(viewState, viewModel::onNewMessage, onBackClick)
}

@Composable
fun ProgressScreen(
    viewState: ProgressScreenViewState,
    onNewMessage: (ProgressScreenFeature.Message) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnBackClick by rememberUpdatedState(newValue = onBackClick)
    val currentOnNewMessage by rememberUpdatedState(newValue = onNewMessage)
    Scaffold(
        topBar = {
            HyperskillTopAppBar(
                title = stringResource(id = org.hyperskill.app.R.string.progress_screen_title),
                onNavigationIconClick = currentOnBackClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.consumeWindowInsets(WindowInsets.statusBars)
        ) {
            if (!viewState.isInErrorState) {
                ProgressScreenContent(
                    viewState = viewState,
                    onNewMessage = currentOnNewMessage,
                    padding = padding
                )
            } else {
                val onRetryClick = remember(currentOnNewMessage) {
                    {
                        currentOnNewMessage(ProgressScreenFeature.Message.RetryContentLoading)
                    }
                }
                ScreenDataLoadingError(
                    onRetryClick = onRetryClick,
                    modifier = Modifier.safeContentPadding()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProgressScreenContent(
    viewState: ProgressScreenViewState,
    onNewMessage: (ProgressScreenFeature.Message) -> Unit,
    padding: PaddingValues
) {
    val onRefresh = remember(onNewMessage) {
        {
            onNewMessage(ProgressScreenFeature.Message.PullToRefresh)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewState.isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        Modifier
            .pullRefresh(pullRefreshState)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding + ProgressDefaults.ScreenPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            TrackProgress(
                viewState = viewState.trackProgressViewState,
                onNewMessage = onNewMessage
            )
            Spacer(modifier = Modifier.height(ProgressDefaults.BetweenBlockSpaceDp))
            ProjectProgress(
                viewState = viewState.projectProgressViewState,
                onNewMessage = onNewMessage
            )
        }

        HyperskillPullRefreshIndicator(
            refreshing = viewState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
@Preview(
    name = "Nexus S",
    showBackground = true,
    device = "id:Nexus S"
)
fun NexusTrackProgressScreenPreview() {
    HyperskillTheme {
        ProgressScreen(
            ProgressScreenViewState(
                trackProgressViewState = ProgressPreview.trackContentViewStatePreview(),
                projectProgressViewState = ProgressPreview.projectContentViewStatePreview(),
                isRefreshing = false
            ),
            onNewMessage = {},
            onBackClick = {}
        )
    }
}

@Composable
@Preview(
    name = "Error",
    showBackground = true
)
fun GeneralTrackProgressScreenPreview() {
    HyperskillTheme {
        ProgressScreen(
            ProgressScreenViewState(
                trackProgressViewState = ProgressPreview.trackErrorPreview(),
                projectProgressViewState = ProgressPreview.projectErrorPreview(),
                isRefreshing = false
            ),
            onNewMessage = {},
            onBackClick = {}
        )
    }
}
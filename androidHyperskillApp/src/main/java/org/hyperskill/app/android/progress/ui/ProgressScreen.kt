package org.hyperskill.app.android.progress.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.android.R
import org.hyperskill.app.android.progress.ui.project.ProjectProgress
import org.hyperskill.app.android.progress.ui.track.TrackProgress
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@OptIn(ExperimentalLifecycleComposeApi::class)
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
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = org.hyperskill.app.R.string.progress_screen_title))
            },
            navigationIcon = {
                IconButton(
                    onClick = currentOnBackClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_toolbar_back),
                        contentDescription = stringResource(id = org.hyperskill.app.R.string.back),
                        tint = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
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
            Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        }
    }
}

@Composable
@Preview(
    name = "Nexus S",
    showBackground = true,
    device = "id:Nexus S"
)
fun NexusTrackProgressScreenPreview() {
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

@Composable
@Preview(
    name = "General",
    showBackground = true
)
fun GeneralTrackProgressScreenPreview() {
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
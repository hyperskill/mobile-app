package org.hyperskill.app.android.track_progress.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.android.R
import org.hyperskill.app.android.track_progress.ui.project.ProjectProgress
import org.hyperskill.app.android.track_progress.ui.track.TrackProgress
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel
import org.hyperskill.app.progresses.view.ProgressScreenViewState
import org.hyperskill.app.projects.domain.model.ProjectLevel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressScreenViewModel
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    ProgressScreen(viewState)
}


@Composable
fun ProgressScreen(
    viewState: ProgressScreenViewState
) {
    val scrollState = rememberScrollState()
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_toolbar_back),
                    contentDescription = stringResource(id = org.hyperskill.app.R.string.back)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            TrackProgress(viewState = viewState.trackProgressViewState)
            Spacer(modifier = Modifier.height(ProgressDefaults.BetweenBlockSpaceDp))
            ProjectProgress(viewState = viewState.projectProgressViewState)
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
            trackProgressViewState = previewTrackViewState(),
            projectProgressViewState = previewProjectViewState(),
            isRefreshing = false
        )
    )
}

@Composable
@Preview(
    name = "General",
    showBackground = true
)
fun GeneralTrackProgressScreenPreview() {
    ProgressScreenViewState(
        trackProgressViewState = previewTrackViewState(),
        projectProgressViewState = previewProjectViewState(),
        isRefreshing = false
    )
}

private fun previewTrackViewState() = ProgressScreenViewState.TrackProgressViewState.Content(
    title = "Kotlin for Beginners",
    imageSource = null,
    completedTopicsCountLabel = "0 / 147",
    completedTopicsPercentageLabel = "34%",
    completedTopicsPercentageProgress = 0.34f,
    appliedTopicsCountLabel = "34 / 150",
    appliedTopicsPercentageLabel = "67",
    appliedTopicsPercentageProgress = 0.67f,
    timeToCompleteLabel = "~ 56h",
    completedGraduateProjectsCount = 50,
    isCompleted = false
)

private fun previewProjectViewState() = ProgressScreenViewState.ProjectProgressViewState.Content(
    title = "Simple Chatty Bot",
    level = ProjectLevel.EASY,
    timeToCompleteLabel = "~ 56h",
    completedStagesLabel = "0 / 5",
    completedStagesProgress = 0.2f,
    isCompleted = false
)
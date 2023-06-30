package org.hyperskill.app.android.track_progress.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@Composable
fun ProjectProgress(
    viewState: ProgressScreenViewState.ProjectProgressViewState,
    modifier: Modifier = Modifier
) {
    when (viewState) {
        ProgressScreenViewState.ProjectProgressViewState.Idle,
        ProgressScreenViewState.ProjectProgressViewState.Empty -> {
            // no op
        }
        ProgressScreenViewState.ProjectProgressViewState.Loading ->
            ProjectProgressSkeleton(modifier)
        is ProgressScreenViewState.ProjectProgressViewState.Content ->
            ProjectProgressContent(viewState, modifier)
        ProgressScreenViewState.ProjectProgressViewState.Error -> {
            // TODO
        }
    }
}
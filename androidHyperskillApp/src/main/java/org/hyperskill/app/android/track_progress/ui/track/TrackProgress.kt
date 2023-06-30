package org.hyperskill.app.android.track_progress.ui.track

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@Composable
fun TrackProgress(
    viewState: ProgressScreenViewState.TrackProgressViewState,
    modifier: Modifier = Modifier
) {
    when (viewState) {
        ProgressScreenViewState.TrackProgressViewState.Idle -> {
            // no op
        }
        ProgressScreenViewState.TrackProgressViewState.Loading ->
            TrackProgressSkeleton(modifier)
        is ProgressScreenViewState.TrackProgressViewState.Content ->
            TrackProgressContent(viewState, modifier)
        ProgressScreenViewState.TrackProgressViewState.Error -> {
            // TODO
        }
    }
}
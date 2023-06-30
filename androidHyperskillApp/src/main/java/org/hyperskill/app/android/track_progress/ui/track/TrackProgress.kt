package org.hyperskill.app.android.track_progress.ui.track

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.hyperskill.app.android.core.view.ui.widget.compose.DataLoadingError
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@Composable
fun TrackProgress(
    viewState: ProgressScreenViewState.TrackProgressViewState,
    onNewMessage: (ProgressScreenFeature.Message) -> Unit,
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
            val onRetryTrackProgressLoadingClick = remember {
                {
                    onNewMessage(ProgressScreenFeature.Message.RetryTrackProgressLoading)
                }
            }
            DataLoadingError(
                onRetryClick = onRetryTrackProgressLoadingClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
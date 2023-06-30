package org.hyperskill.app.android.track_progress.ui.project

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.hyperskill.app.android.core.view.ui.widget.compose.DataLoadingError
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@Composable
fun ProjectProgress(
    viewState: ProgressScreenViewState.ProjectProgressViewState,
    onNewMessage: (ProgressScreenFeature.Message) -> Unit,
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
            val onRetryProjectProgressLoadingClick = remember {
                {
                    onNewMessage(ProgressScreenFeature.Message.RetryProjectProgressLoading)
                }
            }
            DataLoadingError(
                onRetryClick = onRetryProjectProgressLoadingClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
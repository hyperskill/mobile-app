package org.hyperskill.app.android.progress.ui.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.android.progress.ui.BlockHeader
import org.hyperskill.app.android.progress.ui.GeneralStatistics
import org.hyperskill.app.android.progress.ui.ProgressDefaults
import org.hyperskill.app.android.progress.ui.ProgressPreview
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState
import org.hyperskill.app.progress_screen.view.ProgressScreenViewState.TrackProgressViewState.Content.AppliedTopicsState

@Composable
fun TrackProgressContent(
    viewState: ProgressScreenViewState.TrackProgressViewState.Content,
    onNewMessage: (ProgressScreenFeature.Message) -> Unit,
    modifier: Modifier = Modifier
) {
    val onChangeTrackClick = remember(onNewMessage) {
        {
            onNewMessage(ProgressScreenFeature.Message.ChangeTrackButtonClicked)
        }
    }
    Column(modifier = modifier) {
        BlockHeader(
            title = viewState.title,
            iconSource = viewState.imageSource
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        TrackTopicsStatistics(
            countString = viewState.completedTopicsCountLabel,
            percentageString = viewState.completedTopicsPercentageLabel,
            description = stringResource(id = org.hyperskill.app.R.string.progress_screen_completed_topics),
            progressPercent = viewState.completedTopicsPercentageProgress,
            isTrackCompleted = viewState.isCompleted,
            icon = painterResource(id = R.drawable.ic_track_progress_topics)
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.SmallSpaceDp))
        (viewState.appliedTopicsState as? AppliedTopicsState.Content)?.let { appliedTopicsStateContentState ->
            TrackTopicsStatistics(
                countString = appliedTopicsStateContentState.countLabel,
                percentageString = appliedTopicsStateContentState.percentageLabel,
                description = stringResource(id = org.hyperskill.app.R.string.progress_screen_applied_core_topics),
                progressPercent = appliedTopicsStateContentState.percentageProgress,
                isTrackCompleted = viewState.isCompleted,
                icon = painterResource(id = R.drawable.ic_track_progress_core_topics)
            )
        }

        Spacer(modifier = Modifier.height(ProgressDefaults.SmallSpaceDp))
        Row(horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.SmallSpaceDp)) {
            val timeToCompleteLabel = viewState.timeToCompleteLabel
            if (timeToCompleteLabel != null) {
                GeneralStatistics(
                    title = timeToCompleteLabel,
                    icon = painterResource(id = R.drawable.ic_track_progress_time),
                    description = stringResource(
                        id = org.hyperskill.app.R.string.progress_screen_time_to_complete_track
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            viewState.completedGraduateProjectsCount?.let { completedGraduateProjectsCount ->
                if (completedGraduateProjectsCount > 0) {
                    GeneralStatistics(
                        title = viewState.completedGraduateProjectsCount.toString(),
                        icon = painterResource(id = R.drawable.ic_track_progress_graduate_projects),
                        description = stringResource(
                            id = org.hyperskill.app.R.string.progress_screen_completed_graduate_project
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        TextButton(
            onClick = onChangeTrackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = org.hyperskill.app.R.string.progress_screen_change_track))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TrackProgressContentPreview() {
    TrackProgressContent(
        viewState = ProgressPreview.trackContentViewStatePreview(isCompleted = true),
        onNewMessage = {}
    )
}
package org.hyperskill.app.android.progress.ui.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.android.progress.ui.BlockHeader
import org.hyperskill.app.android.progress.ui.GeneralStatistics
import org.hyperskill.app.android.progress.ui.ProgressDefaults
import org.hyperskill.app.android.progress.ui.ProgressPreview
import org.hyperskill.app.progresses.view.ProgressScreenViewState

@Composable
fun TrackProgressContent(
    viewState: ProgressScreenViewState.TrackProgressViewState.Content,
    modifier: Modifier = Modifier
) {
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
        TrackTopicsStatistics(
            countString = viewState.appliedTopicsCountLabel,
            percentageString = viewState.appliedTopicsPercentageLabel,
            description = stringResource(id = org.hyperskill.app.R.string.progress_screen_applied_core_topics),
            progressPercent = viewState.appliedTopicsPercentageProgress,
            isTrackCompleted = viewState.isCompleted,
            icon = painterResource(id = R.drawable.ic_track_progress_core_topics)
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.SmallSpaceDp))
        Row(horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.SmallSpaceDp)) {
            val timeToCompleteLabel = viewState.timeToCompleteLabel
            if (timeToCompleteLabel != null) {
                GeneralStatistics(
                    title = timeToCompleteLabel,
                    icon = painterResource(id = R.drawable.ic_track_progress_time),
                    description = stringResource(id = org.hyperskill.app.R.string.progress_screen_time_to_complete_track),
                    modifier = Modifier.weight(1f)
                )
            }
            if (viewState.completedGraduateProjectsCount > 0) {
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
}

@Composable
@Preview(showBackground = true)
fun TrackProgressContentPreview() {
    TrackProgressContent(
        viewState = ProgressPreview.trackContentViewStatePreview(isCompleted = true)
    )
}
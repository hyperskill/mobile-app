package org.hyperskill.app.android.progress.ui.project

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.android.progress.ui.BlockHeader
import org.hyperskill.app.android.progress.ui.GeneralStatistics
import org.hyperskill.app.android.progress.ui.PercentStatistics
import org.hyperskill.app.android.progress.ui.ProgressDefaults
import org.hyperskill.app.progresses.view.ProgressScreenViewState
import org.hyperskill.app.projects.domain.model.ProjectLevel

@Composable
fun ProjectProgressContent(
    viewState: ProgressScreenViewState.ProjectProgressViewState.Content,
    modifier: Modifier = Modifier
) {
    val levelDrawable by remember(viewState.level) {
        mutableStateOf(viewState.level?.let(::getProjectLevelIconRes))
    }
    Column(modifier = modifier) {
        BlockHeader(
            title = viewState.title,
            painter = levelDrawable?.let { res -> painterResource(res) }
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        Row(horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.SmallSpaceDp)) {
            val timeToCompleteLabel = viewState.timeToCompleteLabel
            if (timeToCompleteLabel != null) {
                GeneralStatistics(
                    title = timeToCompleteLabel,
                    icon = painterResource(id = R.drawable.ic_track_progress_time),
                    description = stringResource(
                        id = org.hyperskill.app.R.string.progress_screen_time_to_complete_project
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            PercentStatistics(
                title = {
                    Text(text = viewState.completedStagesLabel)
                },
                description = stringResource(id = org.hyperskill.app.R.string.progress_screen_stages),
                progressPercent = viewState.completedStagesProgress,
                isTrackCompleted = viewState.isCompleted,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@DrawableRes
private fun getProjectLevelIconRes(level: ProjectLevel): Int =
    when (level) {
        ProjectLevel.EASY -> R.drawable.ic_project_details_level_easy
        ProjectLevel.MEDIUM -> R.drawable.ic_project_details_medium
        ProjectLevel.HARD -> R.drawable.ic_project_details_hard
        ProjectLevel.NIGHTMARE -> R.drawable.ic_project_details_challenging
    }

@Composable
@Preview
fun ProjectProgressPreview() {
    ProjectProgressContent(
        viewState = ProgressScreenViewState.ProjectProgressViewState.Content(
            title = "Simple Chatty Bot",
            level = ProjectLevel.EASY,
            timeToCompleteLabel = "~ 56 h",
            completedStagesLabel = "1 / 5",
            completedStagesProgress = 0.2f,
            isCompleted = false
        )
    )
}

package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus

@Composable
fun ChallengeProgress(
    progressStatuses: List<ProgressStatus>,
    modifier: Modifier = Modifier
) {
    val windowedProgresses = remember(progressStatuses) {
        progressStatuses.windowed(
            size = ChallengeCardDefaults.PROGRESS_ITEMS_IN_ROW,
            step = ChallengeCardDefaults.PROGRESS_ITEMS_IN_ROW,
            partialWindows = true
        )
    }

    /**
     * Column of Rows is used instead of LazyVerticalGrid because
     * LazyVerticalGrid has a bug of not correct horizontal alignment of items.
     * I was fixed in the androidx.compose.foundation:foundation:1.5.0.
     * For more details see https://issuetracker.google.com/issues/267942510
     */
    BoxWithConstraints(modifier = modifier) {
        val consumedSizeInRow =
            ChallengeCardDefaults.progressItemSize.width * ChallengeCardDefaults.PROGRESS_ITEMS_IN_ROW
        val horizontalGapSize = (this.maxWidth - consumedSizeInRow) / (ChallengeCardDefaults.PROGRESS_ITEMS_IN_ROW - 1)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            windowedProgresses.forEachIndexed { lineIndex, progressLine ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(horizontalGapSize),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    progressLine.forEachIndexed { itemIndex, progressStatus ->
                        key(lineIndex, itemIndex) {
                            ChallengeProgressItem(status = progressStatus)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeProgressItem(
    status: ProgressStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .requiredSize(ChallengeCardDefaults.progressItemSize)
            .clip(RoundedCornerShape(4.dp))
            .applyStatusModifiers(status)
    ) {
        if (status == ProgressStatus.MISSED) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.ic_missed_challenge_day),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun Modifier.applyStatusModifiers(status: ProgressStatus): Modifier =
    this.composed {
        when (status) {
            ProgressStatus.COMPLETED ->
                background(colorResource(id = R.color.color_primary))
            ProgressStatus.MISSED, ProgressStatus.INACTIVE ->
                background(colorResource(id = R.color.color_on_surface_alpha_9))
            ProgressStatus.ACTIVE ->
                background(colorResource(id = R.color.color_on_surface_alpha_9))
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.color_primary),
                        shape = RoundedCornerShape(4.dp)
                    )
        }
    }

private class ChallengeProgressItemPreviewProvider : PreviewParameterProvider<ProgressStatus> {
    override val values: Sequence<ProgressStatus>
        get() = ProgressStatus.values().asSequence()
}

@Preview(showBackground = true)
@Composable
fun ChallengeProgressItemPreview(
    @PreviewParameter(ChallengeProgressItemPreviewProvider::class)
    status: ProgressStatus
) {
    HyperskillTheme {
        ChallengeProgressItem(status)
    }
}

@Preview(showBackground = true)
@Composable
private fun ChallengeProgressPreview() {
    HyperskillTheme {
        ChallengeProgress(ChallengeCardPreviewValues.progress)
    }
}
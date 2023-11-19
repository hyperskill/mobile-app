package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun HappeningNowChallengeCard(
    title: String,
    dateText: String,
    description: String,
    timeTitle: String,
    timeSubtitle: String,
    progress: List<ProgressStatus>,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = title,
            dateText = dateText,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_announcment,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeDescription(description)
        ChallengeProgress(progress)
        ChallengeTimeText(title = timeTitle, subtitle = timeSubtitle)
    }
}

@Preview
@Composable
fun HappeningNowChallengeCardPreview() {
    HyperskillTheme {
        HappeningNowChallengeCard(
            title = ChallengeCardPreviewValues.TITLE,
            dateText = ChallengeCardPreviewValues.DATE_TEXT,
            description = ChallengeCardPreviewValues.DESCRIPTION,
            timeTitle = ChallengeCardPreviewValues.TIME_TITLE,
            timeSubtitle = ChallengeCardPreviewValues.TIME_SUBTITLE,
            progress = ChallengeCardPreviewValues.progress
        )
    }
}
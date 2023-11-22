package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState

@Composable
fun NotCompletedChallengeCard(
    state: ChallengeWidgetViewState.Content.Ended,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = state.headerData.title,
            dateText = state.headerData.formattedDurationOfTime,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_not_completed,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeStatus(text = state.headerData.description)
    }
}

@Preview
@Composable
private fun NotCompletedChallengeCardPreview() {
    HyperskillTheme {
        NotCompletedChallengeCard(
            ChallengeWidgetViewState.Content.Ended(
                headerData = ChallengeCardPreviewValues.statusHeaderData
            )
        )
    }
}
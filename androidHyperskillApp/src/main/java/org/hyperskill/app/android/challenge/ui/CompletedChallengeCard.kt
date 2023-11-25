package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState.Content.CollectRewardButtonState

@Composable
fun CompletedChallengeCard(
    state: ChallengeWidgetViewState.Content.Completed,
    onCollectRewardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = state.headerData.title,
            dateText = state.headerData.formattedDurationOfTime,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_completed,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeStatus(text = state.headerData.description)

        val collectRewardButtonState = state.collectRewardButtonState
        if (collectRewardButtonState is CollectRewardButtonState.Visible) {
            HyperskillButton(
                onClick = onCollectRewardClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = collectRewardButtonState.title)
            }
        }
    }
}

@Preview
@Composable
private fun CompletedChallengeCardPreview() {
    HyperskillTheme {
        CompletedChallengeCard(
            ChallengeWidgetViewState.Content.Completed(
                headerData = ChallengeCardPreviewValues.statusHeaderData,
                collectRewardButtonState = CollectRewardButtonState.Visible(
                    title = stringResource(id = R.string.challenge_widget_collect_reward_button_title)
                ),
                isLoadingMagicLink = false
            ),
            onCollectRewardClick = {}
        )
    }
}
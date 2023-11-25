package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState.Content.HappeningNow.CompleteInState

@Composable
fun HappeningNowChallengeCard(
    state: ChallengeWidgetViewState.Content.HappeningNow,
    onReloadClick: () -> Unit,
    onDescriptionLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = state.headerData.title,
            dateText = state.headerData.formattedDurationOfTime,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_progress,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeDescription(
            description = state.headerData.description,
            onLinkClick = onDescriptionLinkClick
        )
        ChallengeProgress(state.progressStatuses)
        when (val completeIn = state.completeInState) {
            CompleteInState.Empty -> {
                // no op
            }
            CompleteInState.Deadline -> {
                HyperskillButton(
                    onClick = onReloadClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.challenge_widget_reload_button
                        )
                    )
                }
            }
            is CompleteInState.TimeRemaining -> {
                ChallengeTimeText(title = completeIn.title, subtitle = completeIn.subtitle)
            }
        }
    }
}

private class HappeningNowChallengeCardPreviewProvider : PreviewParameterProvider<CompleteInState> {
    override val values: Sequence<CompleteInState>
        get() = sequenceOf(
            CompleteInState.Empty,
            CompleteInState.TimeRemaining(
                title = ChallengeCardPreviewValues.TIME_TITLE,
                subtitle = ChallengeCardPreviewValues.TIME_SUBTITLE
            ),
            CompleteInState.Deadline
        )
}

@Preview
@Composable
fun HappeningNowChallengeCardPreview(
    @PreviewParameter(HappeningNowChallengeCardPreviewProvider::class) completeInState: CompleteInState
) {
    HyperskillTheme {
        HappeningNowChallengeCard(
            state = ChallengeWidgetViewState.Content.HappeningNow(
                headerData = ChallengeCardPreviewValues.headerData,
                completeInState = completeInState,
                progressStatuses = ChallengeCardPreviewValues.progress
            ),
            onReloadClick = {},
            onDescriptionLinkClick = {}
        )
    }
}
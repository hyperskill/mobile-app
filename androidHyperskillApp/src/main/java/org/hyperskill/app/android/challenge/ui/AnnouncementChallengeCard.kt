package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState.Content.Announcement

@Composable
fun AnnouncementChallengeCard(
    state: Announcement,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = state.headerData.title,
            dateText = state.headerData.formattedDurationOfTime,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_announcment,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeDescription(state.headerData.description)
        when (val startIn = state.startsInState) {
            Announcement.StartsInState.Deadline -> {
                HyperskillButton(
                    onClick = onReloadClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            id = org.hyperskill.app.R.string.challenge_widget_reload_button
                        )
                    )
                }
            }
            is Announcement.StartsInState.TimeRemaining -> {
                ChallengeTimeText(title = startIn.title, subtitle = startIn.subtitle)
            }
        }
    }
}

private class AnnouncementChallengeCardPreviewProvider : PreviewParameterProvider<Announcement.StartsInState> {
    override val values: Sequence<Announcement.StartsInState>
        get() = sequenceOf(
            Announcement.StartsInState.TimeRemaining(
                title = ChallengeCardPreviewValues.TIME_TITLE,
                subtitle = ChallengeCardPreviewValues.TIME_SUBTITLE
            ),
            Announcement.StartsInState.Deadline
        )
}

@Preview()
@Composable
private fun AnnouncementChallengeCardPreview(
    @PreviewParameter(AnnouncementChallengeCardPreviewProvider::class) startsIn: Announcement.StartsInState
) {
    HyperskillTheme {
        AnnouncementChallengeCard(
            state = Announcement(
                headerData = ChallengeCardPreviewValues.headerData,
                startsInState = startsIn
            ),
            onReloadClick = {}
        )
    }
}
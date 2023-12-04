package org.hyperskill.app.android.leaderboard.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

object LeaderboardItemDefaults {
    val horizontalPadding: Dp = 16.dp
    val verticalPadding: Dp = 8.dp
    val avatarSize: Dp = 40.dp
    const val PLACE_INFO_WEIGHT: Float = 0.16f
    const val SOLVED_PROBLEM_INFO_WEIGHT: Float = 0.16f
    const val PARTICIPANT_INFO_WEIGHT: Float =
        1f - PLACE_INFO_WEIGHT - SOLVED_PROBLEM_INFO_WEIGHT
}

@Composable
fun LeaderboardItem(
    placeNumber: Int,
    participantAvatarUrl: String?,
    participantName: String,
    solvedProblemsAmount: Int,
    problemsSubtitle: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = colorResource(
                    id = if (isHighlighted) {
                        R.color.color_overlay_blue_alpha_7
                    } else {
                        R.color.color_on_primary
                    }
                )
            )
            .padding(
                horizontal = LeaderboardItemDefaults.horizontalPadding,
                vertical = LeaderboardItemDefaults.verticalPadding
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LeaderboardPlaceInfo(
            placeNumber = placeNumber,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(LeaderboardItemDefaults.PLACE_INFO_WEIGHT)
        )

        ParticipantInfo(
            participantAvatarUrl = participantAvatarUrl,
            participantName = participantName,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(LeaderboardItemDefaults.PARTICIPANT_INFO_WEIGHT)
        )

        SolvedProblemsInfo(
            solvedProblemsAmount = solvedProblemsAmount,
            problemsSubtitle = problemsSubtitle,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(LeaderboardItemDefaults.SOLVED_PROBLEM_INFO_WEIGHT)
        )
    }
}

@Composable
private fun ParticipantInfo(
    participantAvatarUrl: String?,
    participantName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val placeholderPainter =
            painterResource(id = org.hyperskill.app.android.R.drawable.avatar_place_holder)
        AsyncImage(
            model = participantAvatarUrl,
            contentDescription = null,
            placeholder = placeholderPainter,
            error = placeholderPainter,
            modifier = Modifier
                .size(LeaderboardItemDefaults.avatarSize)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.color_on_primary),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(colorResource(id = R.color.color_on_surface_alpha_12))
        )
        Text(
            text = participantName,
            style = MaterialTheme.typography.body1,
            color = colorResource(id = R.color.color_on_surface_alpha_87),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun SolvedProblemsInfo(
    solvedProblemsAmount: Int,
    problemsSubtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = solvedProblemsAmount.toString(),
            style = MaterialTheme.typography.body2,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            modifier = Modifier.align(Alignment.End)
        )
        Text(
            text = problemsSubtitle,
            style = MaterialTheme.typography.body2,
            color = colorResource(id = R.color.color_on_surface_alpha_38),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private data class LeaderboardItemData(
    val placeNumber: Int,
    val participantName: String,
    val solvedProblemsAmount: Int,
    val problemsSubtitle: String,
    val isHighlighted: Boolean
)

private class LeaderboardItemDataProvider : PreviewParameterProvider<LeaderboardItemData> {
    override val values: Sequence<LeaderboardItemData>
        get() = sequenceOf(
            LeaderboardItemData(
                placeNumber = 1,
                participantName = "User 115347",
                solvedProblemsAmount = 10000,
                problemsSubtitle = "problems",
                isHighlighted = false
            ),
            LeaderboardItemData(
                placeNumber = 2,
                participantName = "User 115347",
                solvedProblemsAmount = 8000,
                problemsSubtitle = "problems",
                isHighlighted = false
            ),
            LeaderboardItemData(
                placeNumber = 3,
                participantName = "User 115347352315231235",
                solvedProblemsAmount = 3000,
                problemsSubtitle = "problems",
                isHighlighted = false
            ),
            LeaderboardItemData(
                placeNumber = 4,
                participantName = "User 115347352315231235",
                solvedProblemsAmount = 999,
                problemsSubtitle = "problems",
                isHighlighted = false
            ),
            LeaderboardItemData(
                placeNumber = 20,
                participantName = "User 115347352315231235",
                solvedProblemsAmount = 500,
                problemsSubtitle = "problems",
                isHighlighted = false
            ),
            LeaderboardItemData(
                placeNumber = 9999,
                participantName = "User 115347352315231235",
                solvedProblemsAmount = 1,
                problemsSubtitle = "problem",
                isHighlighted = true
            ),
        )
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardItemPreview(
    @PreviewParameter(LeaderboardItemDataProvider::class) leaderBoardItemData: LeaderboardItemData
) {
    HyperskillTheme {
        LeaderboardItem(
            placeNumber = leaderBoardItemData.placeNumber,
            participantAvatarUrl = null,
            participantName = leaderBoardItemData.participantName,
            solvedProblemsAmount = leaderBoardItemData.solvedProblemsAmount,
            problemsSubtitle = leaderBoardItemData.problemsSubtitle,
            isHighlighted = leaderBoardItemData.isHighlighted
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun LeaderboardItemDarkPreview(
    @PreviewParameter(LeaderboardItemDataProvider::class) leaderBoardItemData: LeaderboardItemData
) {
    HyperskillTheme {
        LeaderboardItem(
            placeNumber = leaderBoardItemData.placeNumber,
            participantAvatarUrl = null,
            participantName = leaderBoardItemData.participantName,
            solvedProblemsAmount = leaderBoardItemData.solvedProblemsAmount,
            problemsSubtitle = leaderBoardItemData.problemsSubtitle,
            isHighlighted = leaderBoardItemData.isHighlighted
        )
    }
}
package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading

@Composable
fun LeaderboardSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(5) { index ->
            LeaderboardSkeletonItem(
                placeNumber = if (index <= 2) index + 1 else null
            )
            if (index == 2) {
                LeaderboardListDivider()
            }
        }
    }
}

@Composable
fun LeaderboardSkeletonItem(
    placeNumber: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = colorResource(id = R.color.color_on_primary))
            .padding(
                horizontal = LeaderboardItemDefaults.horizontalPadding,
                vertical = LeaderboardItemDefaults.verticalPadding
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (placeNumber != null) {
            LeaderboardPlaceInfo(
                placeNumber = placeNumber,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(LeaderboardItemDefaults.PLACE_INFO_WEIGHT)
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(LeaderboardItemDefaults.PLACE_INFO_WEIGHT)
            ) {
                ShimmerLoading(
                    modifier = Modifier
                        .size(width = 8.dp, height = 16.dp)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(LeaderboardItemDefaults.PARTICIPANT_INFO_WEIGHT)
        ) {
            ShimmerLoading(
                modifier = Modifier
                    .size(LeaderboardItemDefaults.avatarSize)
                    .clip(CircleShape)
            )
            ShimmerLoading(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(LeaderboardItemDefaults.SOLVED_PROBLEM_INFO_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            ShimmerLoading(
                modifier = Modifier.size(width = 32.dp, height = 12.dp)
            )
            ShimmerLoading(
                modifier = Modifier.size(width = 48.dp, height = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun LeaderboardSkeletonPreview() {
    HyperskillTheme {
        LeaderboardSkeleton()
    }
}
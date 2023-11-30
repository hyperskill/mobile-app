package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading

@Composable
fun LeaderboardSkeleton(
    modifier: Modifier = Modifier
) {
    Row(

    ) {
        ShimmerLoading(
            modifier = Modifier.size(width = 8.dp, height = 16.dp)
        )
    }
}

@Preview
@Composable
fun LeaderboardSkeletonPreview() {
    HyperskillTheme {
        LeaderboardSkeleton()
    }
}
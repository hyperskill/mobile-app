package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

private object LeaderboardListDividerDefaults {
    const val CIRCLE_AMOUNT: Int = 3

    val circleRadius: Dp = 1.dp
    val circleDiameter: Dp = circleRadius * 2
    val gapBetweenCircles: Dp = 3.dp
    val circlesWidth: Dp = circleDiameter * CIRCLE_AMOUNT + gapBetweenCircles * (CIRCLE_AMOUNT - 1)
}

@Composable
fun LeaderboardListDivider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.color_background))
            .padding(vertical = 13.dp, horizontal = 16.dp)
    ) {
        val circleColor = colorResource(id = R.color.color_on_surface_alpha_60)
        Canvas(
            modifier = Modifier
                .size(
                    width = LeaderboardListDividerDefaults.circlesWidth,
                    height = LeaderboardListDividerDefaults.circleDiameter
                )
                .align(Alignment.TopStart)
        ) {
            repeat(LeaderboardListDividerDefaults.CIRCLE_AMOUNT) { circleIndex ->
                drawCircle(
                    color = circleColor,
                    center = Offset(
                        x = getXOffset(circleIndex),
                        y = size.height / 2
                    )
                )
            }
        }
    }
}

private fun Density.getXOffset(circleIndex: Int): Float =
    LeaderboardListDividerDefaults.circleRadius.toPx() +
        circleIndex * (LeaderboardListDividerDefaults.circleDiameter.toPx() + LeaderboardListDividerDefaults.gapBetweenCircles.toPx())

@Preview
@Composable
fun LeaderboardListDividerPreview() {
    HyperskillTheme {
        LeaderboardListDivider()
    }
}
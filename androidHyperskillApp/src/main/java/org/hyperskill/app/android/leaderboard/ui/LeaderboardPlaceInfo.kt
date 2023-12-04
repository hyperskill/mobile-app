package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R

@Composable
fun LeaderboardPlaceInfo(
    placeNumber: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = placeNumber.toString(),
            style = MaterialTheme.typography.body2,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        if (placeNumber in 1..3) {
            Image(
                painter = painterResource(
                    id = when (placeNumber) {
                        1 -> org.hyperskill.app.android.R.drawable.ic_leaderboard_first_place
                        2 -> org.hyperskill.app.android.R.drawable.ic_leaderboard_second_place
                        3 -> org.hyperskill.app.android.R.drawable.ic_leaderboard_third_place
                        else -> error("Place icon should not be visible for the place number $placeNumber")
                    }
                ),
                contentDescription = null,
                modifier = modifier
                    .requiredSize(24.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
package org.hyperskill.app.android.badges.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.LinearProgressIndicator

@Composable
fun BadgeLevelWithProgress(
    level: String,
    nextLevel: Int?,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = level,
                style = MaterialTheme.typography.caption
            )
            if (nextLevel != null) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_badge_locked),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = nextLevel.toString(),
                        style = MaterialTheme.typography.caption,
                        color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_38),
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
        LinearProgressIndicator(
            progress = progress,
            brush = BadgeDefaults.progressGradientBrush,
            backgroundColor = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_9),
            modifier = Modifier
                .height(BadgeDefaults.progressIndicatorHeight)
                .clip(RoundedCornerShape(BadgeDefaults.progressIndicatorRadius))
        )
    }
}
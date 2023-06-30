package org.hyperskill.app.android.track_progress.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.LinearProgressIndicator

@Composable
fun PercentProgressIndicator(
    progressPercent: Float,
    isTrackCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val notCompletedTrackBrush = remember {
        Brush.horizontalGradient(
            0.0f to Color(0xFF5D72E9),
            1f to Color(0xFFA6BCF4),
            startX = 0f,
            endX = 100f
        )
    }
    val progressIndicatorBackgroundColor =
        colorResource(id = R.color.color_on_surface_alpha_9)
    val progressIndicatorModifier =
        modifier.clip(RoundedCornerShape(dimensionResource(id = org.hyperskill.app.android.R.dimen.corner_radius)))
    if (isTrackCompleted) {
        androidx.compose.material.LinearProgressIndicator(
            progress = progressPercent,
            modifier = progressIndicatorModifier,
            color = colorResource(id = R.color.color_secondary),
            backgroundColor = progressIndicatorBackgroundColor
        )
    } else {
        LinearProgressIndicator(
            progress = progressPercent,
            modifier = progressIndicatorModifier,
            brush = notCompletedTrackBrush,
            backgroundColor = progressIndicatorBackgroundColor
        )
    }
}
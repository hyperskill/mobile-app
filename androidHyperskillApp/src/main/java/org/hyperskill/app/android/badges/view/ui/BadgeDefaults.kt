package org.hyperskill.app.android.badges.view.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

object BadgeDefaults {
    val badgeImageSize: DpSize =
        DpSize(width = 73.dp, height = 86.dp)

    val badgeImageVerticalPadding: Dp =
        16.dp

    val progressGradientBrush: Brush =
        Brush.horizontalGradient(
            listOf(
                Color(0xFF7AB7FE),
                Color(0xFF6C63FF)
            )
        )

    val progressIndicatorHeight: Dp =
        8.dp

    val progressIndicatorRadius: Dp =
        4.dp
}
package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Stable
class ShimmerState(
    val colors: List<Color> = listOf(
        Color.Transparent,
        Color.White.copy(alpha = 0.7f),
        Color.Transparent
    ),
    durationMillis: Int = 1200,
    easing: Easing = FastOutSlowInEasing
) {

    var targetValue: Float by mutableStateOf(-2f)
        private set

    val startOffsetXAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = durationMillis,
        easing = easing
    )

    fun runShimmerAnimation() {
        targetValue = 2f
    }
}

fun Modifier.shimmer(shimmerState: ShimmerState) =
    composed {
        val startOffsetX by animateFloatAsState(
            targetValue = shimmerState.targetValue,
            animationSpec = shimmerState.startOffsetXAnimationSpec,
            label = "shimmer"
        )
        drawWithContent {
            val width = size.width
            val height = size.height
            val offset = startOffsetX * width

            drawContent()
            val brush = Brush.linearGradient(
                colors = shimmerState.colors,
                start = Offset(offset, 0f),
                end = Offset(offset + width, height)
            )
            drawRect(brush)
        }
    }
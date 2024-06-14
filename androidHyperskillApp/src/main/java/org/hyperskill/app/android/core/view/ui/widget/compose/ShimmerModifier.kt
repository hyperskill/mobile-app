package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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

private const val InitialValue = -2f
private const val TargetValue = 2f

private val DefaultColors: List<Color> = listOf(
    Color.Transparent,
    Color.White.copy(alpha = 0.7f),
    Color.Transparent
)

/**
 * Applies shimmer animation to the target Composable.
 * Animation is playing one time.
 * To start animation call [ShimmerShotState.runShimmerAnimation] on the [ShimmerShotState] instance.
 */
fun Modifier.shimmerShot(shimmerState: ShimmerShotState): Modifier =
    composed {
        val startOffsetX by animateFloatAsState(
            targetValue = shimmerState.targetValue,
            animationSpec = shimmerState.animationSpec,
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

fun Modifier.shimmer(
    isLoading: Boolean,
    colors: List<Color> = DefaultColors,
    durationMillis: Int = 2000,
    easing: Easing = FastOutSlowInEasing
): Modifier =
    if (isLoading) {
        shimmer(colors, durationMillis, easing)
    } else {
        this
    }

private fun Modifier.shimmer(
    colors: List<Color>,
    durationMillis: Int,
    easing: Easing
): Modifier =
    composed {
        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = InitialValue,
            targetValue = TargetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = easing
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        drawWithContent {
            val width = size.width
            val height = size.height
            val offset = translateAnimation.value * width

            drawContent()
            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(offset, 0f),
                end = Offset(offset + width, height)
            )
            drawRect(brush)
        }
    }

@Stable
class ShimmerShotState(
    val colors: List<Color> = DefaultColors,
    durationMillis: Int = 1200,
    easing: Easing = FastOutSlowInEasing
) {

    var targetValue: Float by mutableStateOf(InitialValue)
        private set

    val animationSpec: DurationBasedAnimationSpec<Float> = tween(
        durationMillis = durationMillis,
        easing = easing
    )

    fun runShimmerAnimation() {
        targetValue = TargetValue
    }
}
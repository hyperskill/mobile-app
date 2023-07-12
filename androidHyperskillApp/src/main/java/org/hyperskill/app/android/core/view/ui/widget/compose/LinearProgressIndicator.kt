package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Determinate <a href="https://material.io/components/progress-indicators#linear-progress-indicators" class="external" target="_blank">Material Design linear progress indicator</a>.
 *
 * **This is a copy of [androidx.compose.material.LinearProgressIndicator] with the ability to set gradient color of the progress.**
 *
 * Progress indicators express an unspecified wait time or display the length of a process.
 *
 * By default, there is no animation between [progress] values. You can use
 * [ProgressIndicatorDefaults.ProgressAnimationSpec] as the default recommended
 * [AnimationSpec] when animating progress.
 *
 * @param progress The progress of this progress indicator, where 0.0 represents no progress and 1.0
 * represents full progress. Values outside of this range are coerced into the range.
 * @param brush The gradient color of the progress indicator.
 * @param backgroundColor The color of the background behind the indicator, visible when the
 * progress has not reached that area of the overall indicator yet.
 */
@Composable
fun LinearProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    brush: Brush,
    backgroundColor: Color
) {
    Canvas(
        modifier
            .progressSemantics(progress)
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        drawLinearIndicatorBackground(BrushOrColor.Color(backgroundColor), strokeWidth)
        drawLinearIndicator(0f, progress, BrushOrColor.Brush(brush), strokeWidth)
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    brushOrColor: BrushOrColor,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // Progress line
    when (brushOrColor) {
        is BrushOrColor.Brush -> {
            drawLine(brushOrColor.brush, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
        }
        is BrushOrColor.Color -> {
            drawLine(brushOrColor.color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
        }
    }
}

private fun DrawScope.drawLinearIndicatorBackground(
    brushOrColor: BrushOrColor,
    strokeWidth: Float
) =
    drawLinearIndicator(0f, 1f, brushOrColor, strokeWidth)

@Immutable
private sealed interface BrushOrColor {
    class Brush(val brush: androidx.compose.ui.graphics.Brush) : BrushOrColor
    class Color(val color: androidx.compose.ui.graphics.Color) : BrushOrColor
}

private val LinearIndicatorHeight = ProgressIndicatorDefaults.StrokeWidth
private val LinearIndicatorWidth = 240.dp
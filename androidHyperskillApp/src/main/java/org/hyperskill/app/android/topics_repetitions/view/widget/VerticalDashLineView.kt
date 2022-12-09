package org.hyperskill.app.android.topics_repetitions.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import org.hyperskill.app.android.R
import ru.nobird.android.view.base.ui.extension.Dp
import ru.nobird.android.view.base.ui.extension.toPx

/**
 * Custom view to draw dotted, vertical lines.
 *
 * A regular shape drawable (like we use for horizontal lines) is usually not sufficient because
 * rotating such a (by default horizontal) line to be vertical does not recalculate the correct
 * with and height if they are set to match_parent or wrap_content.
 *
 * A more elaborate version of this view would use custom attributes to set the color of the line
 * more dynamically, as well as the line shape, gap size, etc.
 */
class VerticalDashLineView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        private const val DashLength = 5f
        private const val DashGap = 5f
        private const val StrokeWidth = 1f
    }

    private val verticalLinePaint: Paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.color_on_surface_alpha_12)
        style = Paint.Style.FILL
        strokeWidth = Dp(StrokeWidth).toPx().value
        pathEffect = DashPathEffect(
            floatArrayOf(Dp(DashLength).toPx().value, Dp(DashGap).toPx().value),
            0f
        )
    }

    override fun onDraw(canvas: Canvas) {
        val center = width * .5f
        canvas.drawLine(center, 0f, center, height.toFloat(), verticalLinePaint)
    }
}
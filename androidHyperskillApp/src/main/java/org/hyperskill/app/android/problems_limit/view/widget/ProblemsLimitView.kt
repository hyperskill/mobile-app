package org.hyperskill.app.android.problems_limit.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import ru.nobird.android.view.base.ui.extension.Dp
import ru.nobird.android.view.base.ui.extension.toPx
import kotlin.math.roundToInt

class ProblemsLimitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_CIRCLES_COUNT = 5
    }

    private val circleRadius = Dp(4f).toPx().value
    private val circlesMargin = Dp(4f).toPx().value

    private val activeCirclePaint: Paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, org.hyperskill.app.R.color.color_overlay_violet)
        style = Paint.Style.FILL
    }

    private val inactiveCirclePaint: Paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_12)
        style = Paint.Style.FILL
    }

    private var totalCount = DEFAULT_CIRCLES_COUNT
    private var activeCount = 2

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val circleDiameter = circleRadius * 2

        val desiredWidth = (circleDiameter * totalCount + circlesMargin * (totalCount - 1)).roundToInt()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val desiredHeight = circleDiameter.roundToInt()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val circleDiameter = circleRadius * 2
        for (i in 0 until totalCount) {
            canvas.withTranslation(i * (circleDiameter + circlesMargin)) {
                val paint = if (i > activeCount - 1) inactiveCirclePaint else activeCirclePaint
                drawCircle(circleRadius, circleRadius, circleRadius, paint)
            }
        }
    }

    fun setData(totalCount: Int, activeCount: Int) {
        if (this.totalCount == totalCount || this.activeCount == activeCount) {
            return
        }
        this.totalCount = totalCount
        this.activeCount = activeCount
        invalidate()
    }
}
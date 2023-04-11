package org.hyperskill.app.android.gamification_toolbar.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import ru.nobird.android.view.base.ui.extension.Dp
import ru.nobird.android.view.base.ui.extension.toPx

class ToolbarTrackProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundRadius = Dp(14f).toPx().value
    private val circleRadius = Dp(8f).toPx().value
    private val arcStrokeWidth = Dp(2f).toPx().value

    private val uncompletedInactivePaint = Paint().also {
        setupCirclePaint(it, org.hyperskill.app.R.color.color_primary_alpha_38)
    }
    private val uncompletedActivePaint = Paint().also {
        setupCirclePaint(it, org.hyperskill.app.R.color.color_primary)
    }

    private val completedInactivePaint = Paint().also {
        setupCirclePaint(it, org.hyperskill.app.R.color.color_secondary_alpha_38)
    }
    private val completedActiveCompletedPaint = Paint().also {
        setupCirclePaint(it, org.hyperskill.app.R.color.color_secondary)
    }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, org.hyperskill.app.R.color.color_surface)
    }
    private val progressRectF = RectF()

    private var progressSweepAngel: Float = 0f
    private var isCompleted: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Height == width
        val desiredHeight = (backgroundRadius * 2).roundToInt()

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val width: Int = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, widthSize)
            else -> desiredHeight
        }

        val heightSize = MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredHeight, widthSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        progressRectF.apply {
            left = height / 2f - circleRadius
            top = height / 2f - circleRadius
            right = height / 2f + circleRadius
            bottom = height / 2f + circleRadius
        }
        with(canvas) {
            drawCircle(backgroundRadius, backgroundRadius, backgroundRadius, backgroundPaint)
            drawArc(
                /* oval = */ progressRectF,
                /* startAngle = */ -90f,
                /* sweepAngle = */ progressSweepAngel,
                /* useCenter = */ false,
                /* paint = */ if (isCompleted) completedActiveCompletedPaint else uncompletedActivePaint
            )
            drawArc(
                /* oval = */ progressRectF,
                /* startAngle = */ -90f + progressSweepAngel,
                /* sweepAngle = */ 360f - progressSweepAngel,
                /* useCenter = */ false,
                /* paint = */ if (isCompleted) completedInactivePaint else uncompletedInactivePaint
            )
        }
    }

    fun setProgress(progress: Int, isCompleted: Boolean) {
        val normalizedProgress =
            progress.coerceAtMost(100).coerceAtLeast(0)
        val progressRadius = normalizedProgress / 100f * 360
        if (progressRadius != this.progressSweepAngel) {
            this.progressSweepAngel = progressRadius
            this.isCompleted = isCompleted
            invalidate()
        }
    }

    private fun setupCirclePaint(paint: Paint, @ColorRes colorRes: Int) {
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = arcStrokeWidth
            color = ContextCompat.getColor(context, colorRes)
        }
    }
}
package org.hyperskill.app.android.core.view.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.text.DynamicLayout
import android.text.Layout.Alignment.ALIGN_NORMAL
import android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextUtils
import android.text.TextUtils.TruncateAt.END
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import org.hyperskill.app.android.R
import kotlin.math.abs

/**
 * DO NOT directly use android:text or setText in this view.
 * Use app:originalText or originalText instead. Attempting to use android:text or setText will lead to unexpected behaviour.
 *
 * At any time, limitedMaxLines MUST always be less than or equal to maxLines. Otherwise, an exception will be thrown.
 *
 * This view only supports [TextUtils.TruncateAt.END].
 * */
class ExpandableTextView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_LIMITED_MAX_LINES = 3
    }

    var originalText: String = ""
        set(value) {
            field = value
            updateCollapsedDisplayedText(actionTextChanged = false)
        }

    var expandAction: String = ""
        set(value) {
            field = value
            val ellipsis = Typography.ellipsis
            val start = ellipsis.toString().length
            expandActionSpannable = SpannableString("$ellipsis $value")
            expandActionSpannable.setSpan(
                ForegroundColorSpan(expandActionColor),
                start,
                expandActionSpannable.length,
                SPAN_EXCLUSIVE_EXCLUSIVE
            )
            updateCollapsedDisplayedText(actionTextChanged = true)
        }

    var limitedMaxLines: Int = 3
        set(value) {
            checkMaxLinesValue(maxLines, value)
            field = value
            updateCollapsedDisplayedText(actionTextChanged = false)
        }

    @ColorInt
    var expandActionColor: Int = ContextCompat.getColor(context, org.hyperskill.app.R.color.color_primary)
        set(value) {
            field = value
            val colorSpan = ForegroundColorSpan(value)
            val ellipsis = Typography.ellipsis
            val start = ellipsis.toString().length
            expandActionSpannable.setSpan(colorSpan, start, expandActionSpannable.length, SPAN_EXCLUSIVE_EXCLUSIVE)
            updateCollapsedDisplayedText(actionTextChanged = true)
        }

    var collapsed = true
        private set

    val expanded get() = !collapsed

    private var oldTextWidth = 0
    private var animator: Animator? = null
    private var expandActionSpannable = SpannableString("")
    private var expandActionStaticLayout: StaticLayout? = null
    private var collapsedDisplayedText: CharSequence? = null

    init {
        ellipsize = END
        obtainAttributes(attrs)
        setOnClickListener { toggle() }
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        try {
            expandAction = attributes.getString(R.styleable.ExpandableTextView_expandAction) ?: ""
            expandActionColor = attributes.getColor(R.styleable.ExpandableTextView_expandActionColor, expandActionColor)
            originalText = attributes.getString(R.styleable.ExpandableTextView_originalText) ?: originalText
            limitedMaxLines =
                attributes.getInt(R.styleable.ExpandableTextView_limitedMaxLines, DEFAULT_LIMITED_MAX_LINES)
            checkMaxLinesValue(maxLines, limitedMaxLines)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val givenWidth = MeasureSpec.getSize(widthMeasureSpec)
        val textWidth = givenWidth - compoundPaddingStart - compoundPaddingEnd
        if (textWidth == oldTextWidth || animator?.isRunning == true) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        oldTextWidth = textWidth
        updateCollapsedDisplayedText(actionTextChanged = true, textWidth)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setMaxLines(maxLines: Int) {
        checkMaxLinesValue(maxLines, limitedMaxLines)
        super.setMaxLines(maxLines)
        updateCollapsedDisplayedText(actionTextChanged = false)
    }

    private fun checkMaxLinesValue(maxLines: Int, limitedMaxLines: Int) {
        check(maxLines == -1 || limitedMaxLines <= maxLines) {
            """
                maxLines ($maxLines) must be greater than or equal to limitedMaxLines ($limitedMaxLines). 
                maxLines can be -1 if there is no upper limit for lineCount.
            """.trimIndent()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun setEllipsize(where: TextUtils.TruncateAt?) {
        /**
         * Due to this issue https://stackoverflow.com/questions/63939222/constraintlayout-ellipsize-start-not-working,
         * this view only supports TextUtils.TruncateAt.END
         */
        super.setEllipsize(END)
    }

    fun toggle() {
        if (originalText == collapsedDisplayedText) {
            collapsed = !collapsed
            return
        }

        val initialHeight = height
        text = if (collapsed) originalText else collapsedDisplayedText

        measure(MeasureSpec.makeMeasureSpec(width, EXACTLY), MeasureSpec.makeMeasureSpec(height, UNSPECIFIED))

        animator?.cancel()
        val newAnimator = getAnimator(initialHeight = initialHeight, targetHeight = measuredHeight)
        animator = newAnimator
        newAnimator.start()
    }

    private fun getAnimator(initialHeight: Int, targetHeight: Int) =
        ValueAnimator.ofInt(initialHeight, targetHeight).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = (abs(targetHeight - initialHeight) * 2L).coerceAtMost(300L)
            addUpdateListener { value ->
                val newLayoutParams = layoutParams.apply {
                    height = value.animatedValue as Int
                }
                layoutParams = newLayoutParams
            }
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        collapsed = !collapsed
                        text = originalText
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        text = if (collapsed) collapsedDisplayedText else originalText
                        val newLayoutParams = layoutParams.apply {
                            layoutParams.height = WRAP_CONTENT
                        }
                        layoutParams = newLayoutParams
                    }
                }
            )
        }

    private fun resolveDisplayedText(staticLayout: StaticLayout): CharSequence? {
        val truncatedTextWithoutActionText = staticLayout.text
        if (truncatedTextWithoutActionText.toString() != originalText) {
            val totalTextWidthWithoutActionText =
                (0 until staticLayout.lineCount).sumOf { staticLayout.getLineWidth(it).toInt() }
            val totalTextWidthWithActionText = totalTextWidthWithoutActionText - expandActionStaticLayout!!.getLineWidth(0)
            val textWithoutActionText = TextUtils.ellipsize(originalText, paint, totalTextWidthWithActionText, END)
            val defaultEllipsisStart = textWithoutActionText.indexOf(Typography.ellipsis)

            // in case the size only fits ActionText, shows ActionText only
            if (textWithoutActionText == "") return expandActionStaticLayout!!.text

            // on some devices Typography.ellipsis can't be found,
            // in that case don't replace ellipsis sign with ellipsizedText
            // users are still able to expand ellipsized text
            if (defaultEllipsisStart == -1) {
                return truncatedTextWithoutActionText
            }

            val defaultEllipsisEnd = defaultEllipsisStart + 1
            val span = SpannableStringBuilder()
                .append(textWithoutActionText)
                .replace(defaultEllipsisStart, defaultEllipsisEnd, expandActionStaticLayout!!.text)
            return maybeRemoveEndingCharacters(staticLayout, span)
        } else {
            return originalText
        }
    }

    // sanity check before applying the text. Most of the time, the loop doesn't happen
    private fun maybeRemoveEndingCharacters(
        staticLayout: StaticLayout,
        span: SpannableStringBuilder
    ): SpannableStringBuilder {
        val textWidth = staticLayout.width
        val dynamicLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DynamicLayout.Builder.obtain(span, paint, textWidth)
                .setAlignment(ALIGN_NORMAL)
                .setIncludePad(false)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .build()
        } else {
            @Suppress("DEPRECATION")
            DynamicLayout(span, span, paint, textWidth, ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, false)
        }

        val actionTextIndex = span.indexOf(expandActionStaticLayout!!.text.toString())
        var removingCharIndex = actionTextIndex - 1
        while (removingCharIndex >= 0 && dynamicLayout.lineCount > limitedMaxLines) {
            span.delete(removingCharIndex, removingCharIndex + 1)
            removingCharIndex--
        }
        return span
    }

    private fun updateCollapsedDisplayedText(
        actionTextChanged: Boolean,
        textWidth: Int = measuredWidth - compoundPaddingStart - compoundPaddingEnd
    ) {
        if (textWidth <= 0) return
        val collapsedStaticLayout = getStaticLayout(limitedMaxLines, originalText, textWidth)
        if (actionTextChanged) {
            expandActionStaticLayout = getStaticLayout(1, expandActionSpannable, textWidth)
        }
        collapsedDisplayedText = resolveDisplayedText(collapsedStaticLayout)
        text = if (collapsed) collapsedDisplayedText else originalText
    }

    private fun getStaticLayout(targetMaxLines: Int, text: CharSequence, textWidth: Int): StaticLayout {
        val maximumLineWidth = textWidth.coerceAtLeast(0)
        return StaticLayout.Builder
            .obtain(text, 0, text.length, paint, maximumLineWidth)
            .setIncludePad(false)
            .setEllipsize(END)
            .setMaxLines(targetMaxLines)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .build()
    }
}
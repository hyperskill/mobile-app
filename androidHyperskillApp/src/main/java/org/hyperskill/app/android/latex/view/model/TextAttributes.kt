package org.hyperskill.app.android.latex.view.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.appcompat.content.res.AppCompatResources
import org.hyperskill.app.android.R
import org.hyperskill.app.android.view.base.ui.extension.ColorExtensions
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute
import ru.nobird.android.view.base.ui.extension.resolveFloatAttribute
import ru.nobird.android.view.base.ui.extension.toSp

data class TextAttributes(
    val textSize: Float,
    @ColorInt
    val textColor: Int,
    @ColorInt
    val textColorHighlight: Int,
    val textIsSelectable: Boolean,

    @FontRes
    val fontResId: Int,

    val isNightMode: Boolean
) {
    companion object {
        private val attrsSet =
            intArrayOf(
                android.R.attr.textSize,
                android.R.attr.textColor,
                android.R.attr.textColorHighlight,
                android.R.attr.textIsSelectable,
                androidx.appcompat.R.attr.fontFamily,
                R.attr.isNightMode
            ).apply { sort() }

        @SuppressLint("ResourceType")
        fun fromAttributeSet(context: Context, attrs: AttributeSet?): TextAttributes {
            val colorOnSurface = context.resolveColorAttribute(com.google.android.material.R.attr.colorOnSurface)
            val alphaEmphasisHigh = context.resolveFloatAttribute(R.attr.alphaEmphasisHigh)

            // default params
            var textAttributes = TextAttributes(
                textSize = 14f,
                textColor = ColorExtensions.colorWithAlphaMul(colorOnSurface, alphaEmphasisHigh),
                textColorHighlight = context.resolveColorAttribute(android.R.attr.textColorHighlight),
                textIsSelectable = false,
                fontResId = R.font.roboto_regular,
                isNightMode = false
            )

            val textAppearance = obtainTextAppearance(context, attrs)
            if (textAppearance != null) {
                textAttributes = readTypedArray(textAttributes, context, textAppearance)
            }

            val array = context.obtainStyledAttributes(attrs, attrsSet)
            textAttributes = readTypedArray(textAttributes, context, array)

            return textAttributes
        }

        private fun obtainTextAppearance(context: Context, attrs: AttributeSet?): TypedArray? {
            val a = context.theme.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textAppearance), -1, -1)
            val ap = a.getResourceId(0, -1)
            a.recycle()

            return if (ap != -1) {
                context.theme.obtainStyledAttributes(ap, attrsSet)
            } else {
                null
            }
        }

        private fun readTypedArray(
            textAttributes: TextAttributes,
            context: Context,
            array: TypedArray
        ): TextAttributes =
            try {
                val textColor = array.getResourceId(attrsSet.indexOf(android.R.attr.textColor), 0)
                    .takeIf { it != 0 }
                    ?.let { AppCompatResources.getColorStateList(context, it) }
                    ?.defaultColor
                    ?: textAttributes.textColor

                TextAttributes(
                    textSize =
                    array.getDimensionPixelSize(attrsSet.indexOf(android.R.attr.textSize), 0)
                        .takeIf { it > 0 }
                        ?.toFloat()
                        ?.toSp()
                        ?: textAttributes.textSize,

                    textColor = textColor,

                    textColorHighlight = array.getColor(
                        attrsSet.indexOf(android.R.attr.textColorHighlight),
                        textAttributes.textColorHighlight
                    ),

                    textIsSelectable = array.getBoolean(
                        attrsSet.indexOf(android.R.attr.textIsSelectable),
                        textAttributes.textIsSelectable
                    ),

                    fontResId = array.getResourceId(
                        attrsSet.indexOf(androidx.appcompat.R.attr.fontFamily),
                        textAttributes.fontResId
                    ),

                    isNightMode = array.getBoolean(attrsSet.indexOf(R.attr.isNightMode), textAttributes.isNightMode)
                )
            } finally {
                array.recycle()
            }
    }
}
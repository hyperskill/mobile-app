package org.hyperskill.app.android.core.extensions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import org.hyperskill.app.android.HyperskillApp

object ColorUtil {
    @ColorInt
    fun getColorArgb(@ColorRes resourceColor: Int, context: Context = HyperskillApp.getAppContext()): Int =
        ContextCompat.getColor(context, resourceColor)
}
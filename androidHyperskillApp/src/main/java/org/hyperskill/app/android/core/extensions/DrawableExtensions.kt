package org.hyperskill.app.android.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import ru.nobird.android.view.base.ui.extension.resolveResourceIdAttribute

fun Drawable.setTintList(context: Context, @AttrRes attr: Int): Drawable {
    val colorStateList = AppCompatResources.getColorStateList(context, context.resolveResourceIdAttribute(attr))
    val drawable = this.mutate()
    DrawableCompat.setTintList(drawable, colorStateList)
    return drawable
}
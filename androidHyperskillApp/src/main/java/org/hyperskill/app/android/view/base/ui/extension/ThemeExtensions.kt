package org.hyperskill.app.android.view.base.ui.extension

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import org.hyperskill.app.android.R

fun LayoutInflater.wrapWithTheme(
    context: Context,
    @StyleRes themeRes: Int = R.style.AppTheme
): LayoutInflater =
    cloneInContext(ContextThemeWrapper(context, themeRes))
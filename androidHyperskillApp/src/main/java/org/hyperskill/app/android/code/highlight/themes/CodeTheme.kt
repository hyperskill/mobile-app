package org.hyperskill.app.android.code.highlight.themes

import androidx.annotation.ColorInt
import org.hyperskill.app.android.R
import org.hyperskill.app.android.util.ColorUtil

class CodeTheme(
    val name: String,
    val syntax: CodeSyntax,
    @ColorInt val background: Int,
    @ColorInt val lineNumberBackground: Int,
    @ColorInt val lineNumberStroke: Int,
    @ColorInt val lineNumberText: Int,
    @ColorInt val selectedLineBackground: Int,
    @ColorInt val bracketsHighlight: Int = ColorUtil.getColorArgb(R.color.default_code_brackets_highlight_color),
    @ColorInt val errorHighlight: Int = ColorUtil.getColorArgb(R.color.default_code_error_highlight_color)
)
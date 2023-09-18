package org.hyperskill.app.android.code.view

import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt

/**
 * Is used to distinct codeSyntax spans from the other [ForegroundColorSpan]s
 */
class CodeSyntaxSpan(@ColorInt color: Int) : ForegroundColorSpan(color)
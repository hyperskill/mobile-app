package org.hyperskill.app.android.core.extensions

import android.widget.EditText
import kotlin.math.max

fun EditText.insertText(text: String, offset: Int) {
    val start = max(this.selectionStart - offset, 0)
    val end = max(this.selectionEnd, 0)
    this.text.replace(start, end, text, 0, text.length)
}
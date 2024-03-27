package org.hyperskill.app.android.core.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlin.math.max

fun EditText.insertText(text: String, offset: Int) {
    val start = max(this.selectionStart - offset, 0)
    val end = max(this.selectionEnd, 0)
    this.text.replace(start, end, text, 0, text.length)
}

fun EditText.requestFocus(context: Context) {
    requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
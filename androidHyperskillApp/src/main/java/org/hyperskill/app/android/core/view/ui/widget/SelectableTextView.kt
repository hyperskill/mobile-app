package org.hyperskill.app.android.core.view.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class SelectableTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN && selectionStart != selectionEnd) {
            val txt = this.text
            this.text = null
            this.text = txt
        }

        return super.dispatchTouchEvent(event)
    }
}
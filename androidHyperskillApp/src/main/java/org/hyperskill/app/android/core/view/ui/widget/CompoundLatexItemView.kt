package org.hyperskill.app.android.core.view.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import org.hyperskill.app.android.latex.view.widget.LatexView

class CompoundLatexItemView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        const val MAX_CLICK_DURATION = 200
    }

    private var clickDuration: Long = 0
    private var latexText: View? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (latexText == null) {
            latexText = children.firstOrNull { it is LatexView }
        }
        latexText?.dispatchTouchEvent(ev)

        if (ev.action == MotionEvent.ACTION_UP) {
            clickDuration = ev.eventTime - ev.downTime
        }
        super.onTouchEvent(ev)
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = false

    override fun performClick(): Boolean =
        if (clickDuration < MAX_CLICK_DURATION) {
            super.performClick()
        } else {
            false
        }
}
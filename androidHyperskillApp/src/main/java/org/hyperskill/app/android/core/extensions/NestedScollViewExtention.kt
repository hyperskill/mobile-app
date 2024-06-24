package org.hyperskill.app.android.core.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView

private const val DEFAULT_SMOOTH_SCROLL_DURATION = 250

fun NestedScrollView.smoothScrollToBottom(durationMilliseconds: Int = DEFAULT_SMOOTH_SCROLL_DURATION) {
    val scrollView = this
    val childCount = scrollView.childCount
    if (childCount > 0) {
        val view: View = scrollView.getChildAt(childCount - 1)
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        val y = view.bottom + lp.bottomMargin + scrollView.paddingBottom
        scrollView.smoothScrollTo(0, y, durationMilliseconds)
    }
}
package org.hyperskill.app.android.topics.view.adapter_delegate

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.withTranslation
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView

/**
 * Draw "Learn next" badge on top of the first recycler item
 * */
class LearnNextRecyclerItemDecoration(
    private val badge: View,
    private val marginStart: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            measureBadge(badge, parent)
            outRect.top = badge.measuredHeight / 2
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        parent.forEach { view ->
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                c.withTranslation(marginStart.toFloat(), 0f) {
                    badge.draw(c)
                }
                return@forEach
            }
        }
    }

    private fun measureBadge(badge: View, recyclerView: RecyclerView) {
        // Specs for parent
        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(recyclerView.height, View.MeasureSpec.EXACTLY)

        // Specs for child
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            recyclerView.paddingLeft + recyclerView.paddingRight,
            badge.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            recyclerView.paddingTop + recyclerView.paddingBottom,
            badge.layoutParams.height
        )

        badge.measure(childWidthSpec, childHeightSpec)

        badge.layout(0, 0, badge.measuredWidth, badge.measuredHeight)
    }
}
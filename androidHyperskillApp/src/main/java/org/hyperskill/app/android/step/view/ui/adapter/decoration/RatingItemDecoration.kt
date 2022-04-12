package org.hyperskill.app.android.step.view.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RatingItemDecoration(
    private val horizontalMarginEdges: Int,
    private val horizontalMargin: Int,
    private val verticalMargin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent
            .adapter
            ?.itemCount
            ?: return

        with(outRect) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    left = horizontalMarginEdges
                    right = horizontalMargin
                }
                itemCount - 1 -> {
                    left = horizontalMargin
                    right = horizontalMarginEdges
                }
                else -> {
                    left = horizontalMargin
                    right = horizontalMargin
                }
            }
            top = verticalMargin
            bottom = verticalMargin
        }
    }
}
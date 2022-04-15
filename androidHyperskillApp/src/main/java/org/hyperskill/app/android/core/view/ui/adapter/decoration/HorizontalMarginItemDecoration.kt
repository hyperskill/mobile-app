package org.hyperskill.app.android.core.view.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(
    private val horizontalMargin: Int,
    private val firstItemStartMargin: Int = NOT_SET,
    private val lastItemEndMargin: Int = NOT_SET
) : RecyclerView.ItemDecoration() {
    companion object {
        private const val NOT_SET = -1
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            left = if (isFirstItem(parent, view) && firstItemStartMargin != NOT_SET) {
                firstItemStartMargin
            } else {
                horizontalMargin
            }

            right = if (lastItemEndMargin == NOT_SET) {
                horizontalMargin
            } else {
                if (isLastItem(parent, view, state)) {
                    lastItemEndMargin
                } else {
                    horizontalMargin
                }
            }
        }
    }

    private fun isFirstItem(parent: RecyclerView, view: View) =
        parent.getChildAdapterPosition(view) == 0

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State) =
        parent.getChildAdapterPosition(view) == state.itemCount - 1
}
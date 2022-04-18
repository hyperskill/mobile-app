package org.hyperskill.app.android.core.view.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginItemDecoration(
    private val verticalMargin: Int,
    private val firstItemTopMargin: Int = NOT_SET,
    private val lastItemMargin: Int = NOT_SET
) : RecyclerView.ItemDecoration() {
    companion object {
        private const val NOT_SET = -1
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = if (isFirstItem(parent, view) && firstItemTopMargin != NOT_SET) {
                firstItemTopMargin
            } else {
                0
            }

            bottom = if (lastItemMargin == NOT_SET || !isLastItem(parent, view, state)) {
                verticalMargin
            } else {
                lastItemMargin
            }
        }
    }

    private fun isFirstItem(parent: RecyclerView, view: View) =
        parent.getChildAdapterPosition(view) == 0

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State) =
        parent.getChildAdapterPosition(view) == state.itemCount - 1
}
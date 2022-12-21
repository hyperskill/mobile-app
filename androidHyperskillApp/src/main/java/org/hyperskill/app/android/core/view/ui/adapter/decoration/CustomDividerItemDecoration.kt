package org.hyperskill.app.android.core.view.ui.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private typealias SetRectOffset = (position: Int, rect: Rect, state: RecyclerView.State) -> Unit

class CustomDividerItemDecoration(
    private val setRectOffset: SetRectOffset
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            setRectOffset(position, outRect, state)
        }
    }
}

fun RecyclerView.itemDecoration(setRectOffset: SetRectOffset): RecyclerView.ItemDecoration =
    CustomDividerItemDecoration(setRectOffset).also { addItemDecoration(it) }
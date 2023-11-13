package org.hyperskill.app.android.step_quiz_fill_blanks.adapter

import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.LayerListDrawableDelegate
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class FillBlanksSelectItemAdapterDelegate(
    private val options: List<FillBlanksOption>,
    private val onClick: (blankIndex: Int) -> Unit
) : AdapterDelegate<FillBlanksUiItem, DelegateViewHolder<FillBlanksUiItem>>() {
    override fun isForViewType(position: Int, data: FillBlanksUiItem): Boolean =
        data is FillBlanksUiItem.Select

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<FillBlanksUiItem> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_fill_blanks_select))

    inner class ViewHolder(root: View) : DelegateViewHolder<FillBlanksUiItem>(root) {
        private val textView = itemView as TextView
        private val layerListDrawableDelegate = LayerListDrawableDelegate(
            listOf(
                R.id.step_quiz_fill_blanks_select_empty_layer,
                R.id.step_quiz_fill_blanks_select_filled_layer,
                R.id.step_quiz_fill_blanks_select_empty_selected_layer,
                R.id.step_quiz_fill_blanks_select_filled_selected_layer
            ),
            textView.background.mutate() as LayerDrawable
        )

        init {
            textView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position)
                }
            }
        }

        override fun onBind(data: FillBlanksUiItem) {
            val selectItem = data as FillBlanksUiItem.Select
            val selectedOptionIndex = selectItem.origin.selectedOptionIndex
            val text = if (selectedOptionIndex != null) {
                options.getOrNull(selectedOptionIndex)?.displayText
            } else {
                null
            }
            textView.isClickable = selectItem.isEnabled
            textView.setTextIfChanged(text ?: "")
            layerListDrawableDelegate.showLayer(
                when {
                    selectItem.isHighlighted && text.isNullOrEmpty() ->
                        R.id.step_quiz_fill_blanks_select_empty_selected_layer
                    text.isNullOrEmpty() -> R.id.step_quiz_fill_blanks_select_empty_layer
                    else -> R.id.step_quiz_fill_blanks_select_filled_layer
                }
            )
        }
    }
}
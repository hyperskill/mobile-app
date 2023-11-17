package org.hyperskill.app.android.step_quiz_fill_blanks.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksUiItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class FillBlanksInputItemAdapterDelegate(
    private val onClick: (inputItemIndex: Int, text: String) -> Unit
) : AdapterDelegate<FillBlanksUiItem, DelegateViewHolder<FillBlanksUiItem>>() {
    override fun isForViewType(position: Int, data: FillBlanksUiItem): Boolean =
        data is FillBlanksUiItem.Input

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<FillBlanksUiItem> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_fill_blanks_input))

    inner class ViewHolder(root: View) : DelegateViewHolder<FillBlanksUiItem>(root) {
        val textView = itemView as TextView

        init {
            textView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position, textView.text.toString())
                }
            }
        }

        override fun onBind(data: FillBlanksUiItem) {
            val inputItem = data as FillBlanksUiItem.Input
            textView.isClickable = inputItem.isEnabled
            textView.setTextIfChanged(inputItem.origin.inputText ?: "")
        }
    }
}
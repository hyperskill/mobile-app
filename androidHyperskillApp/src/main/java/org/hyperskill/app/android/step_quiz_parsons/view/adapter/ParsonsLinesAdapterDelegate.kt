package org.hyperskill.app.android.step_quiz_parsons.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStepQuizParsonsLineBinding
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ParsonsLinesAdapterDelegate(
    private val onLineClick: (Int) -> Unit
) : AdapterDelegate<ParsonsLine, DelegateViewHolder<ParsonsLine>>() {

    override fun isForViewType(position: Int, data: ParsonsLine): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<ParsonsLine> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_parsons_line))

    private inner class ViewHolder(root: View) : DelegateViewHolder<ParsonsLine>(root) {
        private val viewBinding: ItemStepQuizParsonsLineBinding by viewBinding(ItemStepQuizParsonsLineBinding::bind)

        private val selectedBackground =
            ContextCompat.getDrawable(context, R.drawable.step_quiz_parsons_line_selected_background)
        private val notSelectedBackground =
            ContextCompat.getDrawable(context, R.drawable.step_quiz_parsons_line_background)

        init {
            viewBinding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLineClick(position)
                }
            }
        }

        override fun onBind(data: ParsonsLine) {
            with(viewBinding) {
                stepQuizParsonsLine.setText(data.text)
                root.background = if (data.isSelected) {
                    selectedBackground
                } else {
                    notSelectedBackground
                }
            }
        }
    }
}
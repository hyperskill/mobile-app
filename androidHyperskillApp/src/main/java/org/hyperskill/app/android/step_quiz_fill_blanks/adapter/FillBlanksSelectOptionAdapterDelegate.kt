package org.hyperskill.app.android.step_quiz_fill_blanks.adapter

import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.LayerListDrawableDelegate
import org.hyperskill.app.android.databinding.ItemStepQuizFillBlanksSelectOptionBinding
import org.hyperskill.app.android.step_quiz_fill_blanks.model.FillBlanksSelectOptionUIItem
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksOption
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class FillBlanksSelectOptionAdapterDelegate(
    private val onClick: (selectedOptionIndex: Int, option: FillBlanksOption) -> Unit
) : AdapterDelegate<FillBlanksSelectOptionUIItem, DelegateViewHolder<FillBlanksSelectOptionUIItem>>() {
    override fun isForViewType(position: Int, data: FillBlanksSelectOptionUIItem): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<FillBlanksSelectOptionUIItem> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_fill_blanks_select_option))

    inner class ViewHolder(root: View) : DelegateViewHolder<FillBlanksSelectOptionUIItem>(root) {
        private val binding = ItemStepQuizFillBlanksSelectOptionBinding.bind(itemView)

        private val layerListDrawableDelegate = LayerListDrawableDelegate(
            listOf(
                R.id.step_quiz_fill_blanks_select_empty_layer,
                R.id.step_quiz_fill_blanks_select_filled_layer,
                R.id.step_quiz_fill_blanks_select_empty_selected_layer,
                R.id.step_quiz_fill_blanks_select_filled_selected_layer
            ),
            binding.stepQuizFillBlanksSelectOptionContainer.background.mutate() as LayerDrawable
        )

        init {
            binding.stepQuizFillBlanksSelectOptionContainer.setOnClickListener {
                val position = bindingAdapterPosition
                val item = itemData
                if (position != RecyclerView.NO_POSITION && item != null) {
                    onClick(position, item.option)
                }
            }
        }

        override fun onBind(data: FillBlanksSelectOptionUIItem) {
            with(binding.stepQuizFillBlanksSelectOptionText) {
                isInvisible = data.isUsed
                setTextIfChanged(data.option.displayText)
            }
            binding.stepQuizFillBlanksSelectOptionContainer.isClickable =
                data.isClickable && !data.isUsed
            layerListDrawableDelegate.showLayer(
                if (data.isUsed) {
                    R.id.step_quiz_fill_blanks_select_empty_layer
                } else {
                    R.id.step_quiz_fill_blanks_select_filled_layer
                }
            )
        }
    }
}
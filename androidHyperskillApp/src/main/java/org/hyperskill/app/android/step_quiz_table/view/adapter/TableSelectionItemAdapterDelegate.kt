package org.hyperskill.app.android.step_quiz_table.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemTableSelectionBinding
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class TableSelectionItemAdapterDelegate(
    private val onItemClicked: (Int, String, List<Cell>) -> Unit
) : AdapterDelegate<TableSelectionItem, DelegateViewHolder<TableSelectionItem>>() {
    companion object {
        private const val SEPARATOR = ", "
    }

    override fun isForViewType(position: Int, data: TableSelectionItem): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<TableSelectionItem> =
        ViewHolder(createView(parent, R.layout.item_table_selection))

    private inner class ViewHolder(root: View) : DelegateViewHolder<TableSelectionItem>(root) {
        private val viewBinding: ItemTableSelectionBinding by viewBinding(ItemTableSelectionBinding::bind)
        private val viewOverlay = viewBinding.viewOverlay
        private val stepQuizTableTitle = viewBinding.stepQuizTableTitleText
        private val stepQuizTableTitleProgress = viewBinding.stepQuizTitleProgress
        private val stepQuizTableChoice = viewBinding.stepQuizTableChoiceText
        private val stepQuizTableChoiceProgress = viewBinding.stepQuizChoiceProgress
        private val stepQuizTableChevron = viewBinding.stepQuizTableChevron

        init {
            viewOverlay.setOnClickListener {
                onItemClicked(
                    adapterPosition,
                    (itemData as TableSelectionItem).titleText,
                    (itemData as TableSelectionItem).tableChoices
                )
            }
            stepQuizTableTitle.webViewClient =
                ProgressableWebViewClient(stepQuizTableTitleProgress)
            stepQuizTableChoice.webViewClient =
                ProgressableWebViewClient(stepQuizTableChoiceProgress)
        }

        override fun onBind(data: TableSelectionItem) {
            viewOverlay.isEnabled = data.isEnabled
            stepQuizTableChevron.alpha = if (data.isEnabled) 1f else 0.2f
            stepQuizTableTitle.setText(data.titleText)
            val selectedChoices = data.tableChoices.filter { it.answer }

            stepQuizTableChoice.isVisible = selectedChoices.isNotEmpty()
            stepQuizTableChoice.setText(selectedChoices.joinToString(separator = SEPARATOR) { it.name })
        }
    }
}
package org.hyperskill.app.android.step_quiz_table.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemTableColumnSelectionRadiobuttonBinding
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.ui.adapters.selection.SelectionHelper

class TableColumnSingleSelectionItemAdapterDelegate(
    private val selectionHelper: SelectionHelper,
    private val onClick: (Cell) -> Unit
) : AdapterDelegate<Cell, DelegateViewHolder<Cell>>() {
    override fun isForViewType(position: Int, data: Cell): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Cell> =
        ViewHolder(createView(parent, R.layout.item_table_column_selection_radiobutton))

    private inner class ViewHolder(root: View) : DelegateViewHolder<Cell>(root) {
        private val viewBinding: ItemTableColumnSelectionRadiobuttonBinding by viewBinding(ItemTableColumnSelectionRadiobuttonBinding::bind)
        private val tableColumnRadioButton = viewBinding.tableColumnSelectionRadioButton
        private val tableColumnText = viewBinding.tableColumnSelectionText
        private val tableColumnTextProgress = viewBinding.tableColumnSelectionTextProgress

        init {
            root.setOnClickListener {
                onClick(itemData as Cell)
            }
            tableColumnText.webViewClient = ProgressableWebViewClient(tableColumnTextProgress, tableColumnText.webView)
        }

        override fun onBind(data: Cell) {
            itemView.isSelected = selectionHelper.isSelected(adapterPosition)
            tableColumnRadioButton.isChecked = selectionHelper.isSelected(adapterPosition)
            tableColumnText.setText(data.name)
        }
    }
}
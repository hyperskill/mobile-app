package org.hyperskill.app.android.step_quiz_table.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemCompoundSelectionCheckboxBinding
import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.ui.adapters.selection.SelectionHelper

class TableColumnMultipleSelectionItemAdapterDelegate(
    private val selectionHelper: SelectionHelper,
    private val onClick: (TableChoiceItem) -> Unit
) : AdapterDelegate<TableChoiceItem, DelegateViewHolder<TableChoiceItem>>() {
    override fun isForViewType(position: Int, data: TableChoiceItem): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<TableChoiceItem> =
        ViewHolder(createView(parent, R.layout.item_compound_selection_checkbox))

    private inner class ViewHolder(root: View) : DelegateViewHolder<TableChoiceItem>(root) {
        private val viewBinding: ItemCompoundSelectionCheckboxBinding by viewBinding(
            ItemCompoundSelectionCheckboxBinding::bind
        )
        private val tableColumnCheckBox = viewBinding.compoundSelectionCheckBox
        private val tableColumnText = viewBinding.compoundSelectionText
        private val tableColumnTextProgress = viewBinding.compoundSelectionTextProgress

        init {
            root.setOnClickListener {
                onClick(itemData as TableChoiceItem)
            }
            tableColumnText.webViewClient = ProgressableWebViewClient(tableColumnTextProgress)
        }

        override fun onBind(data: TableChoiceItem) {
            itemView.isSelected = selectionHelper.isSelected(bindingAdapterPosition)
            tableColumnCheckBox.isChecked = selectionHelper.isSelected(bindingAdapterPosition)
            tableColumnText.setText(data.text)
        }
    }
}
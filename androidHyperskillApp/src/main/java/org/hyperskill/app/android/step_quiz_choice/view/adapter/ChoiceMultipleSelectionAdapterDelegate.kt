package org.hyperskill.app.android.step_quiz_choice.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemCompoundSelectionCheckboxBinding
import org.hyperskill.app.android.step_quiz_choice.view.model.Choice
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.resolveResourceIdAttribute

class ChoiceMultipleSelectionAdapterDelegate(
    private val onClick: (Choice) -> Unit
) : AdapterDelegate<Choice, DelegateViewHolder<Choice>>() {
    override fun isForViewType(position: Int, data: Choice): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Choice> {
        val view = createView(parent, R.layout.item_compound_selection_checkbox)
        view.setBackgroundResource(parent.context.resolveResourceIdAttribute(R.attr.selectableItemBackgroundRounded))
        return ViewHolder(view)
    }

    private inner class ViewHolder(root: View) : DelegateViewHolder<Choice>(root) {
        private val viewBinding: ItemCompoundSelectionCheckboxBinding by viewBinding(
            ItemCompoundSelectionCheckboxBinding::bind
        )
        private val choiceRadioButton = viewBinding.compoundSelectionCheckBox
        private val choiceText = viewBinding.compoundSelectionText
        private val choiceTextProgress = viewBinding.compoundSelectionTextProgress

        init {
            itemView.setOnClickListener { itemData?.let(onClick) }
            choiceText.webViewClient = ProgressableWebViewClient(choiceTextProgress)
        }

        override fun onBind(data: Choice) {
            itemView.isEnabled = data.isEnabled
            choiceRadioButton.isEnabled = data.isEnabled
            choiceRadioButton.isChecked = data.isSelected
            choiceText.setText(data.option)
        }
    }
}
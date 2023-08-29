package org.hyperskill.app.android.step_quiz_parsons.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStepQuizParsonsLineBinding
import org.hyperskill.app.android.step_quiz_parsons.view.model.ParsonsLine
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ParsonsLinesAdapterDelegate : AdapterDelegate<ParsonsLine, DelegateViewHolder<ParsonsLine>>() {

    override fun isForViewType(position: Int, data: ParsonsLine): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<ParsonsLine> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_parsons_line))

    private class ViewHolder(root: View) : DelegateViewHolder<ParsonsLine>(root) {
        private val viewBinding: ItemStepQuizParsonsLineBinding by viewBinding(ItemStepQuizParsonsLineBinding::bind)

        override fun onBind(data: ParsonsLine) {
            viewBinding.stepQuizParsonsLine.setText(data.text)
        }
    }
}
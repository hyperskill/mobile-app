package org.hyperskill.app.android.step_quiz.view.ui.adapter

import android.view.View
import android.view.ViewGroup
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step_quiz.view.ui.model.Choice
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ChoicesAdapterDelegate(
    private val onClick: (Choice) -> Unit
) : AdapterDelegate<Choice, DelegateViewHolder<Choice>>() {
    override fun isForViewType(position: Int, data: Choice): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Choice> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_single_choice))

    inner class ViewHolder(
        root: View
    ) : DelegateViewHolder<Choice>(root)
}
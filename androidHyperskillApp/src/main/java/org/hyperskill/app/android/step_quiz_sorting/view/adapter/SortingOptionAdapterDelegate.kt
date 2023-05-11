package org.hyperskill.app.android.step_quiz_sorting.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemStepQuizSortingBinding
import org.hyperskill.app.android.step_quiz_sorting.view.model.SortingOption
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class SortingOptionAdapterDelegate(
    private val adapter: DefaultDelegateAdapter<SortingOption>,
    private val onMoveItemClicked: (position: Int, direction: SortingDirection) -> Unit
) : AdapterDelegate<SortingOption, DelegateViewHolder<SortingOption>>() {

    override fun isForViewType(position: Int, data: SortingOption): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<SortingOption> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_sorting))

    private inner class ViewHolder(root: View) : DelegateViewHolder<SortingOption>(root) {
        private val viewBinding: ItemStepQuizSortingBinding by viewBinding(ItemStepQuizSortingBinding::bind)
        private val stepQuizSortingOption = viewBinding.stepQuizSortingOption
        private val stepQuizSortingOptionProgress = viewBinding.stepQuizSortingOptionProgress
        private val stepQuizSortingOptionUp = viewBinding.stepQuizSortingOptionUp
        private val stepQuizSortingOptionDown = viewBinding.stepQuizSortingOptionDown

        init {
            stepQuizSortingOptionUp.setOnClickListener {
                onMoveItemClicked(adapterPosition, SortingDirection.UP)
            }
            stepQuizSortingOptionDown.setOnClickListener {
                onMoveItemClicked(adapterPosition, SortingDirection.DOWN)
            }

            stepQuizSortingOption.webViewClient =
                ProgressableWebViewClient(stepQuizSortingOptionProgress, stepQuizSortingOption.webView)
        }

        override fun onBind(data: SortingOption) {
            itemView.isEnabled = data.isEnabled
            stepQuizSortingOption.setText(data.option)

            stepQuizSortingOptionUp.isEnabled = data.isEnabled && adapterPosition != 0
            stepQuizSortingOptionUp.alpha = if (stepQuizSortingOptionUp.isEnabled) 1f else 0.2f

            stepQuizSortingOptionDown.isEnabled = data.isEnabled && adapterPosition + 1 != adapter.items.size
            stepQuizSortingOptionDown.alpha = if (stepQuizSortingOptionDown.isEnabled) 1f else 0.2f

            val elevation =
                if (data.isEnabled) context.resources.getDimension(R.dimen.step_quiz_sorting_item_elevation) else 0f
            ViewCompat.setElevation(itemView, elevation)
        }
    }

    enum class SortingDirection {
        UP, DOWN
    }
}
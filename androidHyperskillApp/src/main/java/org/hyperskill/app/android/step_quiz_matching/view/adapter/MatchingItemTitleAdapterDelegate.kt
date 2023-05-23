package org.hyperskill.app.android.step_quiz_matching.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.ItemStepQuizSortingBinding
import org.hyperskill.app.android.step_quiz_matching.view.model.MatchingItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class MatchingItemTitleAdapterDelegate : AdapterDelegate<MatchingItem, DelegateViewHolder<MatchingItem>>() {
    override fun isForViewType(position: Int, data: MatchingItem): Boolean =
        data is MatchingItem.Title

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<MatchingItem> =
        ViewHolder(createView(parent, R.layout.item_step_quiz_sorting))

    private inner class ViewHolder(root: View) : DelegateViewHolder<MatchingItem>(root) {
        private val viewBinding: ItemStepQuizSortingBinding by viewBinding(ItemStepQuizSortingBinding::bind)
        private val stepQuizSortingOption = viewBinding.stepQuizSortingOption
        private val stepQuizSortingOptionProgress = viewBinding.stepQuizSortingOptionProgress
        private val stepQuizSortingOptionUp = viewBinding.stepQuizSortingOptionUp
        private val stepQuizSortingOptionDown = viewBinding.stepQuizSortingOptionDown

        init {

            stepQuizSortingOptionUp.isVisible = false
            stepQuizSortingOptionDown.isVisible = false

            root.layoutParams =
                (root.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    rightMargin = context.resources.getDimensionPixelOffset(R.dimen.step_quiz_matching_item_margin)
                }

            stepQuizSortingOption.webViewClient =
                ProgressableWebViewClient(stepQuizSortingOptionProgress, stepQuizSortingOption.webView)
        }

        override fun onBind(data: MatchingItem) {
            data as MatchingItem.Title
            itemView.isEnabled = data.isEnabled

            stepQuizSortingOption.setText(data.text)

            val elevation =
                if (data.isEnabled) context.resources.getDimension(R.dimen.step_quiz_sorting_item_elevation) else 0f
            ViewCompat.setElevation(itemView, elevation)
        }
    }
}
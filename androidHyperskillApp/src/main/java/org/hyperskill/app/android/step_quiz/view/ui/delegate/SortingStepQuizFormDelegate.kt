package org.hyperskill.app.android.step_quiz.view.ui.delegate

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step_quiz.view.ui.adapter.SortingOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz.view.ui.model.SortingOption
import ru.nobird.app.core.model.swap
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class SortingStepQuizFormDelegate(
    containerView: View,
    private val onQuizChanged: (Unit) -> Unit
) {
    private val quizDescription = containerView.findViewById<TextView>(R.id.stepQuizDescriptionTextView)

    private val optionsAdapter = DefaultDelegateAdapter<SortingOption>()

    init {
        quizDescription.setText(R.string.step_quiz_sorting_description)

        optionsAdapter += SortingOptionAdapterDelegate(optionsAdapter, ::moveSortingOption)

        with(containerView.findViewById<RecyclerView>(R.id.sortingRecycler)) {
            adapter = optionsAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)

            (itemAnimator as? SimpleItemAnimator)
                ?.supportsChangeAnimations = false
        }
    }

    private fun moveSortingOption(position: Int, direction: SortingOptionAdapterDelegate.SortingDirection) {
        val targetPosition =
            when (direction) {
                SortingOptionAdapterDelegate.SortingDirection.UP ->
                    position - 1

                SortingOptionAdapterDelegate.SortingDirection.DOWN ->
                    position + 1
            }

        optionsAdapter.items = optionsAdapter.items.swap(position, targetPosition)
        optionsAdapter.notifyItemChanged(position)
        optionsAdapter.notifyItemChanged(targetPosition)
        onQuizChanged(Unit)
    }
}
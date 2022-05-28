package org.hyperskill.app.android.step_quiz.view.ui.delegate

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.ui.adapter.SortingOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz.view.ui.model.SortingOption
import ru.nobird.app.core.model.swap
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class SortingStepQuizFormDelegate(
    binding: FragmentStepQuizBinding,
    private val onQuizChanged: (Unit) -> Unit
): StepQuizFormDelegate {
    private val quizDescription = binding.stepQuizSelectCountTextView

    private val optionsAdapter = DefaultDelegateAdapter<SortingOption>()

    init {
        quizDescription.setText(R.string.step_quiz_sorting_description)

        optionsAdapter += SortingOptionAdapterDelegate(optionsAdapter, ::moveSortingOption)

        // TODO: Remove before merging to delevop
        optionsAdapter.items = listOf(
            SortingOption(1, "Value 1", true),
            SortingOption(2, "Value 2", true),
            SortingOption(3, "Value 3", true)
        )

        with(binding.stepQuizChoiceRecyclerView.sortingRecycler) {
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

    override fun setState(state: Unit) {}

    override fun createReply() {}
}
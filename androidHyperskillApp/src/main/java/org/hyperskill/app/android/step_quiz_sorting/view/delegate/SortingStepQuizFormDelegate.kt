package org.hyperskill.app.android.step_quiz_sorting.view.delegate

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.databinding.LayoutStepQuizSortingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_sorting.view.adapter.SortingOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz_sorting.view.mapper.SortingOptionMapper
import org.hyperskill.app.android.step_quiz_sorting.view.model.SortingOption
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.core.model.swap

class SortingStepQuizFormDelegate(
    binding: LayoutStepQuizSortingBinding,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val optionsAdapter = DefaultDelegateAdapter<SortingOption>()

    private val sortingOptionMapper = SortingOptionMapper()

    init {
        optionsAdapter += SortingOptionAdapterDelegate(optionsAdapter, ::moveSortingOption)

        with(binding.sortingRecycler) {
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
        onQuizChanged(createReply())
    }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val sortingOptions = sortingOptionMapper
            .mapToSortingOptions(state.attempt, StepQuizResolver.isQuizEnabled(state))

        optionsAdapter.items =
            if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                val ordering =
                    (state.submissionState as StepQuizFeature.SubmissionState.Loaded).submission.reply?.ordering
                        ?: emptyList()
                sortingOptions.sortedBy { ordering.indexOf(it.id) }
            } else {
                sortingOptions
            }
    }

    override fun createReply(): Reply =
        Reply(ordering = optionsAdapter.items.map(SortingOption::id))
}
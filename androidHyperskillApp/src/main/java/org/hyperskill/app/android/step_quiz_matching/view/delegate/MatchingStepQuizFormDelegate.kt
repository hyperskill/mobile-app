package org.hyperskill.app.android.step_quiz_matching.view.delegate

import android.util.TypedValue
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizSortingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.android.step_quiz_matching.view.adapter.MatchingItemOptionAdapterDelegate
import org.hyperskill.app.android.step_quiz_matching.view.adapter.MatchingItemTitleAdapterDelegate
import org.hyperskill.app.android.step_quiz_matching.view.mapper.MatchingItemMapper
import org.hyperskill.app.android.step_quiz_matching.view.model.MatchingItem
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.app.core.model.swap
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class MatchingStepQuizFormDelegate(
    containerBinding: FragmentStepQuizBinding,
    binding: LayoutStepQuizSortingBinding,
    private val onQuizChanged: (ReplyResult) -> Unit
) : StepQuizFormDelegate {
    private val quizDescription = containerBinding.stepQuizDescription
    private val optionsAdapter = DefaultDelegateAdapter<MatchingItem>()
    private val matchingItemMapper = MatchingItemMapper()

    companion object {
        const val SKELETON_TITLE_HEIGHT = 50f
    }

    init {
        quizDescription.setText(R.string.step_quiz_matching_title)

        optionsAdapter += MatchingItemTitleAdapterDelegate()
        optionsAdapter += MatchingItemOptionAdapterDelegate(optionsAdapter, ::moveOption)

        with(binding.sortingSkeleton.firstSkeleton) {
            layoutParams =
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    rightMargin = context.resources.getDimensionPixelOffset(R.dimen.step_quiz_matching_item_margin)
                }
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SKELETON_TITLE_HEIGHT, resources.displayMetrics).toInt()
        }

        with(binding.sortingSkeleton.secondSkeleton) {
            layoutParams =
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    leftMargin = context.resources.getDimensionPixelOffset(R.dimen.step_quiz_matching_item_margin)
                }
        }

        with(binding.sortingRecycler) {
            adapter = optionsAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)

            (itemAnimator as? SimpleItemAnimator)
                ?.supportsChangeAnimations = false
        }
    }

    private fun moveOption(position: Int, direction: MatchingItemOptionAdapterDelegate.SortingDirection) {
        val targetPosition =
            when (direction) {
                MatchingItemOptionAdapterDelegate.SortingDirection.UP ->
                    position - 2

                MatchingItemOptionAdapterDelegate.SortingDirection.DOWN ->
                    position + 2
            }

        optionsAdapter.items = optionsAdapter.items.swap(position, targetPosition)
        optionsAdapter.notifyItemChanged(position)
        optionsAdapter.notifyItemChanged(targetPosition)
        onQuizChanged(createReply())
    }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val matchingItems = matchingItemMapper
            .mapToMatchingItems(state.attempt, StepQuizResolver.isQuizEnabled(state))

        optionsAdapter.items =
            if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                val ordering = (state.submissionState as StepQuizFeature.SubmissionState.Loaded).submission.reply?.ordering ?: emptyList()
                matchingItems.sortedBy {
                    when (it) {
                        is MatchingItem.Title ->
                            it.id * 2

                        is MatchingItem.Option ->
                            ordering.indexOf(it.id) * 2 + 1
                    }
                }
            } else {
                matchingItems
            }
    }

    override fun createReply(): ReplyResult =
        ReplyResult(
            Reply(
                ordering = optionsAdapter
                    .items
                    .filterIsInstance<MatchingItem.Option>()
                    .map(MatchingItem.Option::id)
            ),
            ReplyResult.Validation.Success
        )
}
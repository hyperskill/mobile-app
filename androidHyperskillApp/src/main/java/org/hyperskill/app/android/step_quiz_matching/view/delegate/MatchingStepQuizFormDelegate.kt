package org.hyperskill.app.android.step_quiz_matching.view.delegate

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.databinding.LayoutStepQuizMatchingBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_matching.view.mapper.MatchingItemMapper
import org.hyperskill.app.android.step_quiz_table.view.adapter.TableSelectionItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_table.view.fragment.TableColumnSelectionBottomSheetDialogFragment
import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.core.utils.indexOfFirstOrNull
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.submission
import org.hyperskill.app.submissions.domain.model.Reply
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class MatchingStepQuizFormDelegate(
    binding: LayoutStepQuizMatchingBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val matchingAdapter = DefaultDelegateAdapter<TableSelectionItem>().apply {
        addDelegate(TableSelectionItemAdapterDelegate(::onItemClick))
    }

    init {
        with(binding.matchingRecycler) {
            itemAnimator = null
            adapter = matchingAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun onItemClick(index: Int, rowTitle: String, choiceItems: List<TableChoiceItem>) {
        TableColumnSelectionBottomSheetDialogFragment
            .newInstance(
                index = index,
                rowTitle = rowTitle,
                chosenColumns = choiceItems,
                isCheckBox = false
            )
            .showIfNotExists(fragmentManager, TableColumnSelectionBottomSheetDialogFragment.TAG)
    }

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        matchingAdapter.items = MatchingItemMapper.mapToTableSelectionItems(
            attempt = state.attempt,
            submission = state.submission,
            isEnabled = StepQuizResolver.isQuizEnabled(state)
        )
    }

    override fun createReply(): Reply =
        createReplyInternal(matchingAdapter.items)

    private fun createReplyInternal(items: List<TableSelectionItem>): Reply =
        Reply.matching(
            ordering = items.map { item ->
                item.tableChoices.firstOrNull { it.answer }?.id
            }
        )

    fun updateTableSelectionItem(index: Int, answers: List<TableChoiceItem>) {
        val items = matchingAdapter.items
        val newItems = swapAnswers(currentRowIndex = index, answers = answers, rows = items)
        matchingAdapter.items = newItems
        onQuizChanged(createReplyInternal(newItems))
    }

    private fun swapAnswers(
        currentRowIndex: Int,
        answers: List<TableChoiceItem>,
        rows: List<TableSelectionItem>
    ): List<TableSelectionItem> {
        val selectedAnswerIndex =
            answers
                .indexOfFirstOrNull { it.answer }
                ?: return rows
        val rowIndexToSwap =
            rows.indexOfFirstOrNull { it.tableChoices[selectedAnswerIndex].answer }
        return rows.mutate {
            val currentRow = get(currentRowIndex)
            if (rowIndexToSwap != null) {
                val rowToSwap = get(rowIndexToSwap)
                set(currentRowIndex, currentRow.copy(tableChoices = rowToSwap.tableChoices))
                set(rowIndexToSwap, rowToSwap.copy(tableChoices = currentRow.tableChoices))
            } else {
                set(currentRowIndex, currentRow.copy(tableChoices = answers))
            }
        }
    }
}
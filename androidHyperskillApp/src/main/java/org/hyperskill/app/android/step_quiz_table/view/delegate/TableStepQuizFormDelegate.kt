package org.hyperskill.app.android.step_quiz_table.view.delegate

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.databinding.LayoutStepQuizTableBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_table.view.adapter.TableSelectionItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_table.view.fragment.TableColumnSelectionBottomSheetDialogFragment
import org.hyperskill.app.android.step_quiz_table.view.mapper.TableSelectionItemMapper
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.TableChoiceAnswer
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class TableStepQuizFormDelegate(
    binding: LayoutStepQuizTableBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val tableAdapter = DefaultDelegateAdapter<TableSelectionItem>()

    private val tableSelectionItemMapper = TableSelectionItemMapper()

    private var isCheckBox: Boolean = false

    init {
        tableAdapter += TableSelectionItemAdapterDelegate() { index, rowTitle, chosenColumns ->
            TableColumnSelectionBottomSheetDialogFragment
                .newInstance(index, rowTitle, chosenColumns, isCheckBox)
                .showIfNotExists(fragmentManager, TableColumnSelectionBottomSheetDialogFragment.TAG)
        }
        with(binding.tableRecycler) {
            itemAnimator = null
            adapter = tableAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission

        isCheckBox = state.attempt.dataset?.isCheckbox ?: false
        tableAdapter.items = tableSelectionItemMapper.mapToTableSelectionItems(
            state.attempt,
            submission,
            StepQuizResolver.isQuizEnabled(state)
        )
    }

    override fun createReply(): Reply =
        Reply(
            choices = tableAdapter
                .items
                .map {
                    ChoiceAnswer.Table(
                        TableChoiceAnswer(
                            nameRow = it.titleText,
                            columns = it.tableChoices
                        )
                    )
                }
        )

    fun updateTableSelectionItem(index: Int, columns: List<Cell>) {
        tableAdapter.items = tableAdapter.items.mutate {
            val tableSelectionItem = get(index)
            set(index, tableSelectionItem.copy(tableChoices = columns))
        }
        onQuizChanged(createReply())
    }
}
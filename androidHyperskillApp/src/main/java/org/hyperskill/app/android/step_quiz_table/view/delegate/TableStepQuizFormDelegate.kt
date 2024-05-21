package org.hyperskill.app.android.step_quiz_table.view.delegate

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.databinding.LayoutStepQuizTableBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_table.view.adapter.TableSelectionItemAdapterDelegate
import org.hyperskill.app.android.step_quiz_table.view.fragment.TableColumnSelectionBottomSheetDialogFragment
import org.hyperskill.app.android.step_quiz_table.view.mapper.TableSelectionItemMapper
import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.submission
import org.hyperskill.app.submissions.domain.model.Cell
import org.hyperskill.app.submissions.domain.model.ChoiceAnswer
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.TableChoiceAnswer
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.core.model.mutate

class TableStepQuizFormDelegate(
    binding: LayoutStepQuizTableBinding,
    private val fragmentManager: FragmentManager,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val tableAdapter = DefaultDelegateAdapter<TableSelectionItem>()

    private var isCheckBox: Boolean = false

    init {
        tableAdapter += TableSelectionItemAdapterDelegate { index, rowTitle, chosenColumns ->
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

    override fun setState(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        isCheckBox = state.attempt.dataset?.isCheckbox ?: false
        tableAdapter.items = TableSelectionItemMapper.mapToTableSelectionItems(
            attempt = state.attempt,
            submission = state.submission,
            isEnabled = StepQuizResolver.isQuizEnabled(state)
        )
    }

    override fun createReply(): Reply =
        Reply.table(
            answers = tableAdapter.items.map { item ->
                ChoiceAnswer.Table(
                    TableChoiceAnswer(
                        nameRow = item.titleText,
                        columns = item.tableChoices.map { answerItem ->
                            Cell(
                                id = answerItem.text,
                                answer = answerItem.answer
                            )
                        }
                    )
                )
            }
        )

    fun updateTableSelectionItem(index: Int, columns: List<TableChoiceItem>) {
        tableAdapter.items = tableAdapter.items.mutate {
            val tableSelectionItem = get(index)
            set(index, tableSelectionItem.copy(tableChoices = columns))
        }
        onQuizChanged(createReply())
    }
}
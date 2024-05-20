package org.hyperskill.app.android.step_quiz_table.view.mapper

import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.submissions.domain.model.ChoiceAnswer
import org.hyperskill.app.submissions.domain.model.Submission
import ru.nobird.app.core.model.safeCast

object TableSelectionItemMapper {

    fun mapToTableSelectionItems(
        attempt: Attempt,
        submission: Submission?,
        isEnabled: Boolean
    ): List<TableSelectionItem> =
        attempt
            .dataset
            ?.rows
            ?.mapIndexed { index, row ->
                TableSelectionItem(
                    id = index,
                    titleText = row,
                    tableChoices = mapChoiceAnswersToTableAnswerItem(submission?.reply?.choices, index)
                        ?: mapColumnsToTableAnswerItem(attempt.dataset?.columns)
                        ?: emptyList(),
                    isEnabled = isEnabled
                )
            }
            ?: emptyList()

    private fun mapChoiceAnswersToTableAnswerItem(
        choices: List<ChoiceAnswer>?,
        rowIndex: Int,
    ): List<TableChoiceItem>? =
        choices
            ?.getOrNull(rowIndex)
            ?.safeCast<ChoiceAnswer.Table>()
            ?.tableChoice
            ?.columns
            ?.mapIndexed { index, cell ->
                TableChoiceItem(
                    id = index,
                    text = cell.name,
                    answer = cell.answer
                )
            }

    private fun mapColumnsToTableAnswerItem(columns: List<String>?): List<TableChoiceItem>? =
        columns?.mapIndexed { index, text ->
            TableChoiceItem(
                id = index,
                text = text,
                answer = false
            )
        }
}
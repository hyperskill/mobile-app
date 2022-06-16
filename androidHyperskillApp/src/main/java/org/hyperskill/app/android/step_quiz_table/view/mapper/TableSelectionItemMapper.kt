package org.hyperskill.app.android.step_quiz_table.view.mapper

import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission

class TableSelectionItemMapper {
    fun mapToTableSelectionItems(attempt: Attempt, submission: Submission?, isEnabled: Boolean): List<TableSelectionItem> =
        attempt
            .dataset
            ?.rows
            ?.mapIndexed { index, row ->
                TableSelectionItem(
                    index,
                    row,
                    submission
                        ?.reply
                        ?.tableChoices
                        ?.getOrNull(index)
                        ?.columns
                        ?: attempt.dataset?.columns?.map { Cell(id = it, answer = false) }
                        ?: emptyList(),
                    isEnabled = isEnabled
                )
            }
            ?: emptyList()
}
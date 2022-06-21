package org.hyperskill.app.android.step_quiz_table.view.mapper

import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Cell
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import ru.nobird.app.core.model.safeCast

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
                        ?.choices
                        ?.getOrNull(index)
                        ?.safeCast<ChoiceAnswer.Table>()
                        ?.tableChoice
                        ?.columns
                        ?: attempt.dataset?.columns?.map { Cell(id = it, answer = false) }
                        ?: emptyList(),
                    isEnabled = isEnabled
                )
            }
            ?: emptyList()
}
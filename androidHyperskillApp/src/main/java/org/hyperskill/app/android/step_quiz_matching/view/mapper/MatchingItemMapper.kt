package org.hyperskill.app.android.step_quiz_matching.view.mapper

import org.hyperskill.app.android.step_quiz_table.view.model.TableChoiceItem
import org.hyperskill.app.android.step_quiz_table.view.model.TableSelectionItem
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.Pair
import org.hyperskill.app.submissions.domain.model.Submission

object MatchingItemMapper {

    fun mapToTableSelectionItems(
        attempt: Attempt,
        submission: Submission?,
        isEnabled: Boolean
    ): List<TableSelectionItem> {
        val pairs = attempt.dataset?.pairs ?: return emptyList()
        val ordering = submission?.reply?.ordering
        return pairs.mapIndexed { index, pair ->
            TableSelectionItem(
                id = index,
                titleText = pair.first ?: "",
                tableChoices = mapPairsToTableChoices(pairs, index, ordering),
                isEnabled = isEnabled
            )
        }
    }

    private fun mapPairsToTableChoices(
        pairs: List<Pair>,
        rowIndex: Int,
        ordering: List<Int?>?
    ): List<TableChoiceItem> =
        pairs.mapIndexed { choiceIndex, choicePair ->
            TableChoiceItem(
                id = choiceIndex,
                text = choicePair.second ?: "",
                answer = ordering?.getOrNull(rowIndex) == choiceIndex
            )
        }
}
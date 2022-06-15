package org.hyperskill.app.android.step_quiz_matching.view.mapper

import org.hyperskill.app.android.step_quiz_matching.view.model.MatchingItem
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt

class MatchingItemMapper {
    fun mapToMatchingItems(attempt: Attempt, isEnabled: Boolean): List<MatchingItem> =
        attempt
            .dataset
            ?.pairs
            ?.mapIndexed { index, pair ->
                listOf(
                    MatchingItem.Title(index, pair.first ?: "", isEnabled),
                    MatchingItem.Option(index, pair.second ?: "", isEnabled)
                )
            }
            ?.flatten()
            ?: emptyList()
}
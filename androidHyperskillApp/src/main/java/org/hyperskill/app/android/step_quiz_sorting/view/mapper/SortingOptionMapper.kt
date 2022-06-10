package org.hyperskill.app.android.step_quiz_sorting.view.mapper

import org.hyperskill.app.android.step_quiz_sorting.view.model.SortingOption
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt

class SortingOptionMapper {
    fun mapToSortingOptions(attempt: Attempt, isEnabled: Boolean): List<SortingOption> =
        attempt
            .dataset
            ?.options
            ?.mapIndexed { index, option -> SortingOption(index, option, isEnabled) }
            ?: emptyList()
}
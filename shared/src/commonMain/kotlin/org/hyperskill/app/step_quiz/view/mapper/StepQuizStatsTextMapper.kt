package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider

class StepQuizStatsTextMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getFormattedStepQuizStats(solvedByUsersCount: Int, millisSinceLastCompleted: Long?): String? =
        if (solvedByUsersCount > 0 && millisSinceLastCompleted != null) {
            resourceProvider.getString(
                SharedResources.strings.step_quiz_stats_text,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.users,
                    solvedByUsersCount,
                    solvedByUsersCount
                ),
                DateFormatter.formatTimeDistance(millisSinceLastCompleted)
            )
        } else {
            null
        }
}
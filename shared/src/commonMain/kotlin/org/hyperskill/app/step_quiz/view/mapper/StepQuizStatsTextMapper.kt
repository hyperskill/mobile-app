package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter

class StepQuizStatsTextMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: SharedDateFormatter
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
                dateFormatter.formatTimeDistance(millisSinceLastCompleted)
            )
        } else {
            null
        }
}
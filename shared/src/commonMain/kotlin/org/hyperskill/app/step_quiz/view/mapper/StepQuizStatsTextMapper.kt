package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter

class StepQuizStatsTextMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: SharedDateFormatter
) {
    fun getFormattedStepQuizStats(solvedByCount: Int, millisSinceLastCompleted: Long?): String? =
        if (solvedByCount > 0 && millisSinceLastCompleted != null) {
            resourceProvider.getString(
                SharedResources.strings.step_quiz_stats_text,
                resourceProvider.getQuantityString(
                    SharedResources.plurals.learners,
                    solvedByCount,
                    solvedByCount
                ),
                dateFormatter.formatTimeDistance(millisSinceLastCompleted)
            )
        } else {
            null
        }
}
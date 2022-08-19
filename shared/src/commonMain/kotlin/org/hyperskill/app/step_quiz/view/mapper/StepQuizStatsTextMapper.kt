package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.extension.TimeFancifier

class StepQuizStatsTextMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getFormattedStepQuizStats(users: Int, millisSinceLastCompleted: Long): String =
        resourceProvider.getString(
            SharedResources.strings.step_quiz_stats_text,
            resourceProvider.getQuantityString(SharedResources.plurals.users, users, users),
            TimeFancifier.formatTimeDistance(millisSinceLastCompleted)
        )
}
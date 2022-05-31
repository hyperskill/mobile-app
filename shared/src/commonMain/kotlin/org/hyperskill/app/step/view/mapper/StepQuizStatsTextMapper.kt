package org.hyperskill.app.step.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider

class StepQuizStatsTextMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getFormattedStepQuizStats(users: Int, hours: Int): String =
        resourceProvider.getString(
            SharedResources.strings.step_quiz_stats_text,
            resourceProvider.getQuantityString(SharedResources.plurals.users, users, users),
            resourceProvider.getQuantityString(SharedResources.plurals.hours, hours, hours)
        )
}
package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents step completion analytic event when user has completed a problem.
 *
 * @param stepId Solved problem id.
 * @param trackTitle Title of the track.
 *
 * @see AppsFlyerAnalyticEvent
 */
class StepCompletionStepSolvedAppsFlyerAnalyticEvent(
    stepId: Long,
    trackTitle: String?
) : AppsFlyerAnalyticEvent(name = "af_complete_exercise") {
    companion object {
        private const val PARAM_STEP_ID = "exercise_id"
        private const val PARAM_TRACK_TITLE = "track_name"
    }

    override val params: Map<String, Any> =
        mapOfNotNull(
            PARAM_STEP_ID to stepId,
            PARAM_TRACK_TITLE to trackTitle
        )
}
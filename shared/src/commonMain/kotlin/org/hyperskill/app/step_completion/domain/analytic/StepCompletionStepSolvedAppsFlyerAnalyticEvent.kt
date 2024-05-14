package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents step completion analytic event when a user has completed a problem.
 *
 * @param stepId Solved problem id.
 * @param trackTitle Title of the track.
 *
 * @see AppsFlyerAnalyticEvent
 */
class StepCompletionStepSolvedAppsFlyerAnalyticEvent(
    stepId: Long,
    trackTitle: String?
) : AppsFlyerAnalyticEvent(
    name = "af_complete_exercise",
    params = mapOfNotNull(
        StepCompletionHyperskillAnalyticParams.PARAM_STEP_ID to stepId,
        StepCompletionHyperskillAnalyticParams.PARAM_TRACK_TITLE to trackTitle
    )
)
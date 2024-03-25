package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Start practicing" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "actions",
 *     "target": "start_practicing",
 *     "context":
 *     {
 *         "is_located_at_beginning": true
 *     }
 * }
 * ```
 *
 * @param route A step route where the event occurred.
 * @param isLocatedAtBeginning A flag indicating whether the start practicing button is located at the beginning
 * of the step screen.
 *
 * @see HyperskillAnalyticEvent
 */
class StepCompletionClickedStartPracticingHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isLocatedAtBeginning: Boolean
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.ACTIONS,
    target = HyperskillAnalyticTarget.START_PRACTICING,
    context = mapOf(StepCompletionHyperskillAnalyticParams.PARAM_IS_LOCATED_AT_BEGINNING to isLocatedAtBeginning)
)
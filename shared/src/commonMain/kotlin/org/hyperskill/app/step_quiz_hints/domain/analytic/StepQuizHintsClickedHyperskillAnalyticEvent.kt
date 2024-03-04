package org.hyperskill.app.step_quiz_hints.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a common click analytic event for the StepQuizHintsFeature.
 *
 * Click on the reaction "Yes" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_hints",
 *     "target": "yes",
 *     "context":
 *     {
 *         "comment_id": 1
 *     }
 * }
 * ```
 *
 * Click on the reaction "No" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_hints",
 *     "target": "no",
 *     "context":
 *     {
 *         "comment_id": 1
 *     }
 * }
 * ```
 *
 * Click on the "See hint" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_hints",
 *     "target": "see_hint"
 * }
 * ```
 *
 * Click on the "See next hint" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_hints",
 *     "target": "see_next_hint"
 * }
 * ```
 *
 * Click on the "Report" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "step_hints",
 *     "target": "report",
 *     "context":
 *     {
 *         "comment_id": 1
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizHintsClickedHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    target: HyperskillAnalyticTarget,
    val commentId: Long? = null
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STEP_HINTS,
    target = target,
    context = mapOfNotNull(PARAM_COMMENT_ID to commentId)
) {
    companion object {
        private const val PARAM_COMMENT_ID = "comment_id"
    }
}
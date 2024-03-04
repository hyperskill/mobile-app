package org.hyperskill.app.progress_screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on "Change project" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/progress",
 *     "action": "click",
 *     "part": "main",
 *     "target": "change_project"
 *     "context":
 *     {
 *         "track_id": 1
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProgressScreenClickedChangeProjectHyperskillAnalyticEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Progress(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.CHANGE_PROJECT,
    context = mapOf(PARAM_TRACK_ID to trackId)
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }
}
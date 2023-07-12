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
    private val trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Progress(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.CHANGE_PROJECT
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}
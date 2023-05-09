package org.hyperskill.app.track_list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a common click analytic event for the PlaceholderNewUserFeature.
 *
 * Click on the "Start learning" button:
 * ```
 * {
 *     "route": "/tracks",
 *     "action": "click",
 *     "part": "track_modal",
 *     "target": "start_learning",
 *     "context":
 *     {
 *         "track_id": 1234
 *     }
 * }
 * ```
 *
 * Click on the Track widget:
 * ```
 * {
 *     "route": "/tracks",
 *     "action": "click",
 *     "part": "main",
 *     "target": "track",
 *     "context":
 *     {
 *         "track_id": 1234
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackListClickedHyperskillAnalyticEvent(
    part: HyperskillAnalyticPart,
    target: HyperskillAnalyticTarget,
    val trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks(),
    HyperskillAnalyticAction.CLICK,
    part,
    target
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}
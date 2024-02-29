package org.hyperskill.app.track_selection.list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a track card clicked analytic event.
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
class TrackSelectionListTrackClickedHyperskillAnalyticEvent(
    val trackId: Long
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Tracks(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.TRACK,
    context = mapOf(PARAM_TRACK_ID to trackId)
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }
}
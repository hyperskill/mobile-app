package org.hyperskill.app.track_list.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the new user placeholder track modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks",
 *     "action": "hidden",
 *     "part": "track_modal",
 *     "target": "close",
 *     "context":
 *     {
 *         "track_id": 1234
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackListTrackModalHiddenHyperskillAnalyticEvent(val trackId: Long) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.TRACK_MODAL,
    HyperskillAnalyticTarget.CLOSE
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}
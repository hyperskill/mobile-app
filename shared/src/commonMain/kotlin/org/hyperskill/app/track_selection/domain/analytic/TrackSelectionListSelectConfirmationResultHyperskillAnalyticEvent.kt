package org.hyperskill.app.track_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event for clicking on the confirmation button on the track selection confirmation modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks"
 *     "action": "click",
 *     "part": "track_selection_modal",
 *     "target": "yes / no"
 *     "context":
 *     {
 *         "track_id": 1234
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackSelectionListSelectConfirmationResultHyperskillAnalyticEvent(
    private val trackId: Long,
    isConfirmed: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.TRACK_SELECTION_MODAL,
    if (isConfirmed) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}
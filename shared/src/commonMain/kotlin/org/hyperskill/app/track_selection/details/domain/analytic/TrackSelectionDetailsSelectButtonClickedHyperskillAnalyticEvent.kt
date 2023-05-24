package org.hyperskill.app.track_selection.details.domain.analytic

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
 *     "route": "/tracks/1",
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
class TrackSelectionDetailsSelectButtonClickedHyperskillAnalyticEvent(
    val trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks.Details(trackId),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SELECT_THIS_TRACK
)
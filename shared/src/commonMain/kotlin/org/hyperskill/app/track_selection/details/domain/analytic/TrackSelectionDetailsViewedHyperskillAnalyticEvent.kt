package org.hyperskill.app.track_selection.details.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks/1",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackSelectionDetailsViewedHyperskillAnalyticEvent(
    trackId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks.Details(trackId),
    HyperskillAnalyticAction.VIEW
)
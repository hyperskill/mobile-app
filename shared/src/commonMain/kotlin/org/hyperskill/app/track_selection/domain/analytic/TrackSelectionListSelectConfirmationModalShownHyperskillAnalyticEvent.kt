package org.hyperskill.app.track_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event for the track selection confirmation modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks"
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "track_selection_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackSelectionListSelectConfirmationModalShownHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.TRACK_SELECTION_MODAL
)
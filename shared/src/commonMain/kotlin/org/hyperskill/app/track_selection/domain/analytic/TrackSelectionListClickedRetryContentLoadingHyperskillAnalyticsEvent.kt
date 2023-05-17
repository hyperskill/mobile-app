package org.hyperskill.app.track_selection.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a retry button in the tracks-list.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/tracks",
 *     "action": "click",
 *     "part": "main",
 *     "target": "retry",
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class TrackSelectionListClickedRetryContentLoadingHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Tracks(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.RETRY
)
package org.hyperskill.app.track.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Track(), HyperskillAnalyticAction.VIEW)
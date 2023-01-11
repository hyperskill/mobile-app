package org.hyperskill.app.track.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on the streak navigation bar button item analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track",
 *     "action": "click",
 *     "part": "head",
 *     "target": "streak"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackClickedStreakBarButtonItemHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Track(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.HEAD,
    HyperskillAnalyticTarget.STREAK
)
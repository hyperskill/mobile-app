package org.hyperskill.app.profile.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a PullToRefresh analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "click",
 *     "part": "main",
 *     "target": "refresh"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileClickedPullToRefreshHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.REFRESH
)
package org.hyperskill.app.leaderboard.screen.domain.analytic

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
 *     "route": "/leaderboard",
 *     "action": "click",
 *     "part": "main",
 *     "target": "refresh"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object LeaderboardClickedPullToRefreshHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Leaderboard(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.REFRESH
)
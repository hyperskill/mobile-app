package org.hyperskill.app.leaderboard.screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature

/**
 * Represents a click analytic event of the leaderboard tab.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/leaderboard",
 *     "action": "click",
 *     "part": "head",
 *     "target": "day | week"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class LeaderboardClickedTabHyperskillAnalyticEvent(tab: LeaderboardScreenFeature.Tab) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Leaderboard(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.HEAD,
    target = when (tab) {
        LeaderboardScreenFeature.Tab.DAY -> HyperskillAnalyticTarget.DAY
        LeaderboardScreenFeature.Tab.WEEK -> HyperskillAnalyticTarget.WEEK
    }
)
package org.hyperskill.app.gamification_toolbar.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen

/**
 * Represents a click on the problems limit navigation bar button item analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home | /study-plan | /leaderboard",
 *     "action": "click",
 *     "part": "head",
 *     "target": "problems_limit"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class GamificationToolbarClickedProblemsLimitHSAnalyticEvent(
    screen: GamificationToolbarScreen
) : HyperskillAnalyticEvent(
    screen.analyticRoute,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.HEAD,
    HyperskillAnalyticTarget.PROBLEMS_LIMIT
)
package org.hyperskill.app.leaderboard.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.leaderboard.domain.model.LeaderboardItem
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature

/**
 * Represents a click analytic event of the leaderboard item.
 *
 * @property leaderboardItem clicked leaderboard item
 * @constructor Creates a click analytic event of the leaderboard item.
 *
 * @param currentTab current tab of the leaderboard
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/leaderboard",
 *     "action": "click",
 *     "part": "leaderboard_day_tab | leaderboard_week_tab",
 *     "target": "leaderboard_item",
 *     "context":
 *     {
 *         "user_id": 1,
 *         "position": 10,
 *         "passed_problems": 100
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class LeaderboardWidgetClickedListItemHyperskillAnalyticEvent(
    currentTab: LeaderboardScreenFeature.Tab,
    private val leaderboardItem: LeaderboardItem
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Leaderboard(),
    action = HyperskillAnalyticAction.CLICK,
    part = when (currentTab) {
        LeaderboardScreenFeature.Tab.DAY -> HyperskillAnalyticPart.LEADERBOARD_DAY_TAB
        LeaderboardScreenFeature.Tab.WEEK -> HyperskillAnalyticPart.LEADERBOARD_WEEK_TAB
    },
    target = HyperskillAnalyticTarget.LEADERBOARD_ITEM
) {
    companion object {
        private const val USER_ID = "user_id"
        private const val POSITION = "position"
        private const val PASSED_PROBLEMS = "passed_problems"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    USER_ID to leaderboardItem.user.id,
                    POSITION to leaderboardItem.position,
                    PASSED_PROBLEMS to leaderboardItem.passedProblems
                )
            )
}
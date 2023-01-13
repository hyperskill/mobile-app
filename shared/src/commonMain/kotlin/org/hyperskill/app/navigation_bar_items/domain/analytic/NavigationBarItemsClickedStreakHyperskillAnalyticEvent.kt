package org.hyperskill.app.navigation_bar_items.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.navigation_bar_items.domain.model.NavigationBarItemsScreen

/**
 * Represents a click on the streak navigation bar button item analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track | /home",
 *     "action": "click",
 *     "part": "head",
 *     "target": "streak"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class NavigationBarItemsClickedStreakHyperskillAnalyticEvent(
    screen: NavigationBarItemsScreen
) : HyperskillAnalyticEvent(
    screen.analyticRoute,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.HEAD,
    HyperskillAnalyticTarget.STREAK
)
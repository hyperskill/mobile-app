package org.hyperskill.app.gamification_toolbar.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen

/**
 * Represents a click on the progress navigation bar button item analytic event.
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
 *
 * @constructor Creates a new instance of [GamificationToolbarClickedProgressHyperskillAnalyticEvent].
 * @param screen The screen where the event was triggered.
 *
 * @see HyperskillAnalyticEvent
 */
class GamificationToolbarClickedProgressHyperskillAnalyticEvent(
    screen: GamificationToolbarScreen
) : HyperskillAnalyticEvent(
    screen.analyticRoute,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.HEAD,
    HyperskillAnalyticTarget.PROGRESS
)
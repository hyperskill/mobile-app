package org.hyperskill.app.main.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents first time app launch analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "app-launch-first-time",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object AppLaunchFirstTimeHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.AppLaunchFirstTime(),
    action = HyperskillAnalyticAction.VIEW
)
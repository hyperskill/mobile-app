package org.hyperskill.app.progresses.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a back button in the progress screen.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/progress",
 *     "action": "click",
 *     "part": "main",
 *     "target": "back",
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ProgressScreenClickedBackHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Progress(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.BACK
)
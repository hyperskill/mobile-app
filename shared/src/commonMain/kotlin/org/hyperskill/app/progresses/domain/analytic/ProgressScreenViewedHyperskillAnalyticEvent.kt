package org.hyperskill.app.progresses.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/progress",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProgressScreenViewedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Progress(),
    HyperskillAnalyticAction.VIEW
)
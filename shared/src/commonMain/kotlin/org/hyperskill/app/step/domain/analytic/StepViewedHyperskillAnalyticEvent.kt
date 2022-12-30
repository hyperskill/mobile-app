package org.hyperskill.app.step.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1 | /learn/daily/1 | /repeat/step/1",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepViewedHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(route, HyperskillAnalyticAction.VIEW)
package org.hyperskill.app.stage_implement.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/projects/{PROJECT_ID}/stages/{STAGE_ID}/implement",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StageImplementViewedHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(route, HyperskillAnalyticAction.VIEW)
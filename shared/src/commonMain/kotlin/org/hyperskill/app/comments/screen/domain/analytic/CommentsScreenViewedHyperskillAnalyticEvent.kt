package org.hyperskill.app.comments.screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1#comment",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class CommentsScreenViewedHyperskillAnalyticEvent(
    stepRoute: StepRoute
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Comment(stepRoute),
    action = HyperskillAnalyticAction.VIEW
)
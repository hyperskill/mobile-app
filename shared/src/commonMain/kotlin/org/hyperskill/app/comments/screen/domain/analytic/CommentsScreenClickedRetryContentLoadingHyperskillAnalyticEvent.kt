package org.hyperskill.app.comments.screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents a click analytic event of the error state placeholder retry button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1#comment",
 *     "action": "click",
 *     "part": "main",
 *     "target": "retry"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class CommentsScreenClickedRetryContentLoadingHyperskillAnalyticEvent(
    stepRoute: StepRoute
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Comment(stepRoute),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.RETRY
)
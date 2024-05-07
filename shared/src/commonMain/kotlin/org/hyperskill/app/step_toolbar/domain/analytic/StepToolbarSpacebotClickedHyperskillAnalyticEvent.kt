package org.hyperskill.app.step_toolbar.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event on the spacebot head in the navigation bar.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "head",
 *     "target": "spacebot"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepToolbarSpacebotClickedHyperskillAnalyticEvent(
    analyticRoute: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route = analyticRoute,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.HEAD,
    target = HyperskillAnalyticTarget.SPACEBOT
)
package org.hyperskill.app.topic_completed_modal.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a user did take a screenshot analytic event of the topic completed modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "topic_completed_modal",
 *     "target": "screenshot"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class TopicCompletedModalUserDidTakeScreenshotHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.TOPIC_COMPLETED_MODAL,
    target = HyperskillAnalyticTarget.SCREENSHOT
)
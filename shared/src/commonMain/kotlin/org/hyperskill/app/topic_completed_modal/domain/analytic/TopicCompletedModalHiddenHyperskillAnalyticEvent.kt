package org.hyperskill.app.topic_completed_modal.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the topic completed modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "topic_completed_modal",
 *     "target": "close"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class TopicCompletedModalHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.TOPIC_COMPLETED_MODAL,
    HyperskillAnalyticTarget.CLOSE
)
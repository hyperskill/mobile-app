package org.hyperskill.app.topic_completed_modal.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Continue with next topic" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "topic_completed_modal",
 *     "target": "continue_with_next_topic"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.TOPIC_COMPLETED_MODAL,
    HyperskillAnalyticTarget.CONTINUE_WITH_NEXT_TOPIC
)
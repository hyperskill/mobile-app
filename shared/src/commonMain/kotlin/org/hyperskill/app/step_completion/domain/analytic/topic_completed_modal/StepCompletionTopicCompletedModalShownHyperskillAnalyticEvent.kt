package org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the topic completed modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "topic_completed_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.TOPIC_COMPLETED_MODAL
)
package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a click on the theory toolbar item analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "head",
 *     "target": "theory",
 *     "context":
 *     {
 *         "topic_theory": 1234
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    val topicTheoryId: Long?
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.HEAD,
    HyperskillAnalyticTarget.THEORY
) {
    companion object {
        private const val TOPIC_THEORY = "topic_theory"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOfNotNull(
                    TOPIC_THEORY to topicTheoryId
                )
            )
}
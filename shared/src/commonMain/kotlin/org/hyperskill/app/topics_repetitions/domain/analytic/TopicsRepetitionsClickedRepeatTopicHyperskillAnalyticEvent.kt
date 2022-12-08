package org.hyperskill.app.topics_repetitions.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the repeat topic button (All topics to repeat block) analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/repeat",
 *     "action": "click",
 *     "part": "repeat_topic",
 *     "target": "continue"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TopicsRepetitionsClickedRepeatTopicHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Repeat(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.REPEAT_TOPIC,
    HyperskillAnalyticTarget.CONTINUE
)
package org.hyperskill.app.track.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the theory topic analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/track",
 *     "action": "click",
 *     "part": "theory_to_discover_next",
 *     "target": "topic",
 *     "context":
 *     {
 *         "id": 227,
 *         "theory": 4547
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TrackClickedTopicToDiscoverNextHyperskillAnalyticEvent(
    private val topicId: Long,
    private val theoryId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Track(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.THEORY_TO_DISCOVER_NEXT,
    HyperskillAnalyticTarget.TOPIC
) {
    companion object {
        private const val PARAM_TOPIC_ID = "id"
        private const val PARAM_THEORY_ID = "theory"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_TOPIC_ID to topicId,
                PARAM_THEORY_ID to theoryId
            )
        )
}
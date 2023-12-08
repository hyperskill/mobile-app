package org.hyperskill.app.search.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event on the search results item.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/search",
 *     "action": "click",
 *     "part": "search_results",
 *     "target": "topic",
 *     "context":
 *     {
 *         "query": "test",
 *         "topic_id": 1
 *     }
 * }
 * ```
 *
 * @property query a search query.
 * @property topicId target topic id.
 *
 * @see HyperskillAnalyticEvent
 */
class SearchClickedItemHyperskillAnalyticEvent(
    private val query: String,
    private val topicId: Long
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Search(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.SEARCH_RESULTS,
    HyperskillAnalyticTarget.TOPIC
) {
    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                SearchAnalyticParams.PARAM_QUERY to query,
                SearchAnalyticParams.PARAM_TOPIC_ID to topicId
            )
        )
}
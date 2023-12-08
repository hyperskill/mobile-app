package org.hyperskill.app.search.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event on the "Search" button.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/search",
 *     "action": "click",
 *     "part": "main",
 *     "target": "search",
 *     "context":
 *     {
 *         "query": "test"
 *     }
 * }
 * ```
 *
 * @property query a search query.
 *
 * @see HyperskillAnalyticEvent
 */
class SearchClickedSearchHyperskillAnalyticEvent(
    private val query: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Search(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SEARCH
) {
    companion object {
        private const val PARAM_QUERY = "query"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(PARAM_QUERY to query)
        )
}
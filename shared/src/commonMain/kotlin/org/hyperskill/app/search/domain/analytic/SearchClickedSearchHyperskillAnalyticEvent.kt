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
    route = HyperskillAnalyticRoute.Search(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.SEARCH,
    context = mapOf(SearchAnalyticParams.PARAM_QUERY to query)
)
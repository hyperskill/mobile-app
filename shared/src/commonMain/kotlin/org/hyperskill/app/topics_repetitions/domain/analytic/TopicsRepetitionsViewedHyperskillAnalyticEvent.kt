package org.hyperskill.app.topics_repetitions.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/repeat",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class TopicsRepetitionsViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Repeat(), HyperskillAnalyticAction.VIEW)
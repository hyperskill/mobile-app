package org.hyperskill.app.interview_preparation.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home/interview_preparation_widget",
 *     "action": "view"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object InterviewPreparationWidgetViewedHyperskillAnalyticsEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home.InterviewPreparationWidget(),
    HyperskillAnalyticAction.VIEW
)
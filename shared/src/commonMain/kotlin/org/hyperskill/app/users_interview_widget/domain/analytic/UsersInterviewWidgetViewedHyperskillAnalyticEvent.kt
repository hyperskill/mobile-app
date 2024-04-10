package org.hyperskill.app.users_interview_widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event of the users interview widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan/users-interview-widget",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object UsersInterviewWidgetViewedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan.UsersInterviewWidget(),
    HyperskillAnalyticAction.VIEW
)
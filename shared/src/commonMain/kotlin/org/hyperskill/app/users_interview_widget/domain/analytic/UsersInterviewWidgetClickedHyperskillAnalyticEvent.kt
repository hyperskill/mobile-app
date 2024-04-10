package org.hyperskill.app.users_interview_widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a click analytic event of the users interview widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan",
 *     "action": "click",
 *     "part": "users_interview_widget"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object UsersInterviewWidgetClickedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.USERS_INTERVIEW_WIDGET
)
package org.hyperskill.app.users_questionnaire.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event of the users questionnaire widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home/users-questionnaire-widget",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object UsersQuestionnaireWidgetViewedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home.UsersQuestionnaireWidget(),
    HyperskillAnalyticAction.VIEW
)
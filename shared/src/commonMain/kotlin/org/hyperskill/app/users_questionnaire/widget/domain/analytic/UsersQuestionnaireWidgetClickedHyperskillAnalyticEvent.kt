package org.hyperskill.app.users_questionnaire.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a click analytic event of the users questionnaire widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "users_questionnaire_widget"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object UsersQuestionnaireWidgetClickedHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.USERS_QUESTIONNAIRE_WIDGET
)
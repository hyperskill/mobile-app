package org.hyperskill.app.users_questionnaire.widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents an analytic event for clicking on a close button in the users questionnaire widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan",
 *     "action": "click",
 *     "part": "users_questionnaire_widget",
 *     "target": "close"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object UsersQuestionnaireWidgetClickedCloseHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.USERS_QUESTIONNAIRE_WIDGET,
    HyperskillAnalyticTarget.CLOSE
)
package org.hyperskill.app.users_questionnaire_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the choice analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/questionnaire",
 *     "action": "click",
 *     "part": "main",
 *     "target": "choice",
 *     "context":
 *     {
 *         "source": "Google Play"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class UsersQuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent(
    selectedChoice: String
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.UsersQuestionnaire,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.CHOICE,
    context = mapOf(UsersQuestionnaireOnboardingAnalyticParams.PARAM_SOURCE to selectedChoice)
)
package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/questionnaire",
 *     "action": "view",
 *     "context":
 *     {
 *         "type": "how_did_you_hear_about_hyperskill"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class WelcomeOnboardingUserQuestionnaireViewedHSAnalyticEvent(
    questionnaireType: WelcomeQuestionnaireType
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.UsersQuestionnaire,
    HyperskillAnalyticAction.VIEW,
    context = mapOf(TYPE_KEY to questionnaireType.name.lowercase())
) {
    companion object {
        private const val TYPE_KEY = "type"
    }
}
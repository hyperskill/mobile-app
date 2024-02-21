package org.hyperskill.app.questionnaire_onboarding.domain.analytic

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
class QuestionnaireOnboardingClickedChoiceHyperskillAnalyticEvent(
    private val selectedChoice: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.Questionnaire,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.CHOICE
) {
    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(QuestionnaireOnboardingAnalyticParams.PARAM_SOURCE to selectedChoice)
            )
}
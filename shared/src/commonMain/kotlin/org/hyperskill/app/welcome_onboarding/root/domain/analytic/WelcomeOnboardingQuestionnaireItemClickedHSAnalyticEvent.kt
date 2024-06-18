package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType

/**
 * Represents click on the questionnaire item button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/questionnaire",
 *     "action": "click",
 *     "part": "main",
 *     "target": "questionnaire_item",
 *     "context": {
 *         "questionnaire_type": "how_did_you_hear_about_hyperskill"
 *         "option": "tik_tok"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class WelcomeOnboardingQuestionnaireItemClickedHSAnalyticEvent(
    questionnaireType: WelcomeQuestionnaireType,
    questionnaireItemType: WelcomeQuestionnaireItemType
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.UsersQuestionnaire,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.QUESTIONNAIRE_ITEM,
    context = mapOf(
        QUESTIONNAIRE_TYPE_KEY to questionnaireType.name.lowercase(),
        OPTION_KEY to when (questionnaireItemType) {
            is WelcomeQuestionnaireItemType.ClientSource -> questionnaireItemType.name.lowercase()
            is WelcomeQuestionnaireItemType.CodingBackground -> questionnaireItemType.name.lowercase()
            is WelcomeQuestionnaireItemType.LearningGoal -> questionnaireItemType.name.lowercase()
        }
    )
) {
    companion object {
        private const val QUESTIONNAIRE_TYPE_KEY = "questionnaire_type"
        private const val OPTION_KEY = "option"
    }
}
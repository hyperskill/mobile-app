package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage

/**
 * Represents click on the programming language button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/programming-language-select",
 *     "action": "click",
 *     "part": "main",
 *     "target": "programming_language",
 *     "context": {
 *         "programming_language": "python"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class WelcomeOnboardingProgrammingLanguageClickedHSAnalyticEvent(
    programmingLanguage: WelcomeOnboardingProgrammingLanguage
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.ProgrammingLanguageQuestionnaire,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.PROGRAMMING_LANGUAGE,
    context = mapOf(
        PROGRAMMING_LANGUAGE_KEY to programmingLanguage.name.lowercase()
    )
) {
    companion object {
        private const val PROGRAMMING_LANGUAGE_KEY = "programming_language"
    }
}
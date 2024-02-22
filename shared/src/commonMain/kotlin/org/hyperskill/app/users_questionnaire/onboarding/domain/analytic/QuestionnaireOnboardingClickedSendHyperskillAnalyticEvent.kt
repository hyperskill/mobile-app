package org.hyperskill.app.users_questionnaire.onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents click on the "Send" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/questionnaire",
 *     "action": "click",
 *     "part": "main",
 *     "target": "send",
 *     "context":
 *     {
 *         "source": "Google Play",
 *         "input": "Some text"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class QuestionnaireOnboardingClickedSendHyperskillAnalyticEvent(
    private val selectedChoice: String,
    private val textInputValue: String?
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.Questionnaire,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SEND
) {
    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOfNotNull(
                    QuestionnaireOnboardingAnalyticParams.PARAM_SOURCE to selectedChoice,
                    QuestionnaireOnboardingAnalyticParams.PARAM_INPUT to textInputValue
                )
            )
}
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
class UsersQuestionnaireOnboardingClickedSendHyperskillAnalyticEvent(
    private val selectedChoice: String,
    private val textInputValue: String?
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.UsersQuestionnaire,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.SEND,
    context = mapOfNotNull(
        UsersQuestionnaireOnboardingAnalyticParams.PARAM_SOURCE to selectedChoice,
        UsersQuestionnaireOnboardingAnalyticParams.PARAM_INPUT to textInputValue
    )
)
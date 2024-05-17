package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

/**
 * Represents a shown analytic event of the problem onboarding modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "parsons_problem_onboarding_modal"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ProblemOnboardingModalShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    modalType: StepQuizFeature.ProblemOnboardingModal
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    modalType.hyperskillAnalyticTarget
)

internal val StepQuizFeature.ProblemOnboardingModal.hyperskillAnalyticTarget: HyperskillAnalyticTarget
    get() = when (this) {
        StepQuizFeature.ProblemOnboardingModal.Parsons -> HyperskillAnalyticTarget.PARSONS_PROBLEM_ONBOARDING_MODAL
        StepQuizFeature.ProblemOnboardingModal.GptCodeGenerationWithErrors ->
            HyperskillAnalyticTarget.GPT_CODE_GENERATION_WITH_ERRORS_ONBOARDING_MODAL
    }
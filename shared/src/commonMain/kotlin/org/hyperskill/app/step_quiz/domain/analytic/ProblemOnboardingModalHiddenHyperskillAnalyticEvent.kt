package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode

/**
 * Represents a hidden analytic event of the problem onboarding modal.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "parsons_problem_onboarding_modal",
 *     "target": "close"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ProblemOnboardingModalHiddenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    modalType: StepQuizFeature.ProblemOnboardingModal
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    modalType.hyperskillAnalyticPart,
    HyperskillAnalyticTarget.CLOSE
)

internal val StepQuizFeature.ProblemOnboardingModal.hyperskillAnalyticPart: HyperskillAnalyticPart
    get() = when (this) {
        StepQuizFeature.ProblemOnboardingModal.Parsons -> HyperskillAnalyticPart.PARSONS_PROBLEM_ONBOARDING_MODAL
        is StepQuizFeature.ProblemOnboardingModal.FillBlanks ->
            when (this.mode) {
                FillBlanksMode.INPUT -> HyperskillAnalyticPart.FILL_BLANKS_INPUT_MODE_ONBOARDING_MODAL
                FillBlanksMode.SELECT -> HyperskillAnalyticPart.FILL_BLANKS_SELECT_MODE_ONBOARDING_MODAL
            }
        StepQuizFeature.ProblemOnboardingModal.GptCodeGenerationWithErrors ->
            HyperskillAnalyticPart.GPT_CODE_GENERATION_WITH_ERRORS_ONBOARDING_MODAL
    }
package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode

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
        is StepQuizFeature.ProblemOnboardingModal.FillBlanks ->
            when (this.mode) {
                FillBlanksMode.INPUT -> HyperskillAnalyticTarget.FILL_BLANKS_INPUT_MODE_ONBOARDING_MODAL
                FillBlanksMode.SELECT -> HyperskillAnalyticTarget.FILL_BLANKS_SELECT_MODE_ONBOARDING_MODAL
            }
    }
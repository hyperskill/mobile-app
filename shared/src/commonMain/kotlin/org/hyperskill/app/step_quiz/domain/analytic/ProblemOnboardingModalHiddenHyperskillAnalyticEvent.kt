package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

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
    route = route,
    action = HyperskillAnalyticAction.HIDDEN,
    part = modalType.hyperskillAnalyticPart,
    target = HyperskillAnalyticTarget.CLOSE
)

internal val StepQuizFeature.ProblemOnboardingModal.hyperskillAnalyticPart: HyperskillAnalyticPart
    get() = when (this) {
        StepQuizFeature.ProblemOnboardingModal.Parsons -> HyperskillAnalyticPart.PARSONS_PROBLEM_ONBOARDING_MODAL
    }
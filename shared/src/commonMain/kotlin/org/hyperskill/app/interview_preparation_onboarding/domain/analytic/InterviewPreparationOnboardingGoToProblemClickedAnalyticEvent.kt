package org.hyperskill.app.interview_preparation_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to first problem" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/interview-preparation",
 *     "action": "click",
 *     "part": "main",
 *     "target": "go_to_first_problem"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object InterviewPreparationOnboardingGoToProblemClickedAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.InterviewPreparation,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.GO_TO_FIRST_PROBLEM
)
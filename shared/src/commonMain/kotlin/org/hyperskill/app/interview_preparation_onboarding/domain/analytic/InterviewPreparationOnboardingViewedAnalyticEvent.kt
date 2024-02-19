package org.hyperskill.app.interview_preparation_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/interview-preparation",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object InterviewPreparationOnboardingViewedAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.InterviewPreparation,
    HyperskillAnalyticAction.VIEW
)
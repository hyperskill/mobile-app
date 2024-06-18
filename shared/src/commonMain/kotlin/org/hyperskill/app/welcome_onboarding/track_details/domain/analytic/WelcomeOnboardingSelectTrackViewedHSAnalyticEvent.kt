package org.hyperskill.app.welcome_onboarding.track_details.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents a view analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/track-select",
 *     "action": "view"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object WelcomeOnboardingSelectTrackViewedHSAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding.ProgrammingLanguageQuestionnaire,
    HyperskillAnalyticAction.VIEW
)
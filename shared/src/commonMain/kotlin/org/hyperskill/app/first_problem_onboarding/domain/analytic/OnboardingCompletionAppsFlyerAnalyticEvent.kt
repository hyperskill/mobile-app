package org.hyperskill.app.first_problem_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents onboarding completion analytic event.
 *
 * @see AppsFlyerAnalyticEvent
 */
object OnboardingCompletionAppsFlyerAnalyticEvent : AppsFlyerAnalyticEvent(
    name = "af_onb_completion"
)
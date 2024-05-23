package org.hyperskill.app.first_problem_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents onboarding completion analytic event.
 *
 * @param target can be "start_learning" or "keep_learning"
 *
 * @see AppsFlyerAnalyticEvent
 */
class OnboardingCompletionAppsFlyerAnalyticEvent(
    target: String
) : AppsFlyerAnalyticEvent(
    name = "af_onb_completion",
    params = mapOf(PARAM_TARGET to target)
) {
    companion object {
        private const val PARAM_TARGET = "af_target"
    }
}
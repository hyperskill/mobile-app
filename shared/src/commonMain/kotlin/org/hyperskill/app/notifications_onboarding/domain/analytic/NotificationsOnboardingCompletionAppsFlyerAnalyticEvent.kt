package org.hyperskill.app.notifications_onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents onboarding completion analytic event.
 *
 * @param isSuccess true if onboarding was completed successfully, false otherwise.
 *
 * @see AppsFlyerAnalyticEvent
 */
class NotificationsOnboardingCompletionAppsFlyerAnalyticEvent(
    isSuccess: Boolean
) : AppsFlyerAnalyticEvent(
    name = "af_onb_completion",
    params = mapOf(PARAM_SUCCESS to if (isSuccess) "yes" else "no")
) {
    companion object {
        private const val PARAM_SUCCESS = "af_success"
    }
}
package org.hyperskill.app.welcome_onboarding.track_details.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

/**
 * Represents a select track button clicked analytic event.
 *
 * Click on the Track widget:
 * ```
 * {
 *     "route": "/onboarding/track-select",
 *     "action": "click",
 *     "part": "main",
 *     "target": "track",
 *     "context":
 *     {
 *         "track": "python"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class WelcomeOnboardingSelectTrackClickedHSAnalyticEvent(
    track: WelcomeOnboardingTrack
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.TrackSelection,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.TRACK,
    context = mapOf(
        TRACK_KEY to track.name.lowercase()
    )
) {
    companion object {
        private const val TRACK_KEY = "track"
    }
}
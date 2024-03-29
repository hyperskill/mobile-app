package org.hyperskill.app.profile.domain.analytic.badges

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.profile.presentation.ProfileFeature

/**
 * Represents click on the showAll or showLess badges button in profile analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "click",
 *     "part": "main",
 *     "target": "badges_visibility_button",
 *     "context": {
 *         button: "show_all"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class ProfileClickedBadgesVisibilityButtonHyperskillAnalyticEvent(
    private val visibilityButton: ProfileFeature.Message.BadgesVisibilityButton
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Profile(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.BADGES_VISIBILITY_BUTTON,
    context = mapOf(
        PARAM_BUTTON to when (visibilityButton) {
            ProfileFeature.Message.BadgesVisibilityButton.SHOW_ALL -> SHOW_ALL
            ProfileFeature.Message.BadgesVisibilityButton.SHOW_LESS -> SHOW_LESS
        }
    )
) {
    companion object {
        private const val PARAM_BUTTON = "button"
        private const val SHOW_ALL = "show_all"
        private const val SHOW_LESS = "show_less"
    }
}
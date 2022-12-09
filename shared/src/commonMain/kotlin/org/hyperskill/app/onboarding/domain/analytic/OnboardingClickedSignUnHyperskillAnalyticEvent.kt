package org.hyperskill.app.onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Sign up" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding",
 *     "action": "click",
 *     "part": "main",
 *     "target": "sign_up"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class OnboardingClickedSignUnHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SIGN_UP
)
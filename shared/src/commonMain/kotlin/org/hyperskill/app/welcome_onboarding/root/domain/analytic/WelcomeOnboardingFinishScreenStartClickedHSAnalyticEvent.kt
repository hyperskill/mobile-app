package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Start my journey" button on the finish onboarding screen analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding/finish",
 *     "action": "click",
 *     "part": "main",
 *     "target": "start_my_journey"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object WelcomeOnboardingFinishScreenStartClickedHSAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Onboarding.Finish,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.START_MY_JOURNEY
)
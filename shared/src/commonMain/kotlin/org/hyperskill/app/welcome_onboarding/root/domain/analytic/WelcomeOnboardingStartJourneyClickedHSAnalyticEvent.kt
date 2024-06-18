package org.hyperskill.app.welcome_onboarding.root.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Start my journey" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/onboarding",
 *     "action": "click",
 *     "part": "main",
 *     "target": "start_my_journey"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
object WelcomeOnboardingStartJourneyClickedHSAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Onboarding(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.START_MY_JOURNEY
)
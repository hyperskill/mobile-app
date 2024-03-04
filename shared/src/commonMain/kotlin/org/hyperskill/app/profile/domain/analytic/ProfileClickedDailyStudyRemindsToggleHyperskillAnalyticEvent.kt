package org.hyperskill.app.profile.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the daily study reminds toggle analytic event, e.g.e when toggle value changed.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile",
 *     "action": "click",
 *     "part": "main",
 *     "target": "daily_study_reminds",
 *     "context":
 *     {
 *         "state": true
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileClickedDailyStudyRemindsToggleHyperskillAnalyticEvent(
    val isEnabled: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Profile(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.DAILY_STUDY_REMINDS,
    context = mapOf(PARAM_STATE to isEnabled)
) {
    companion object {
        private const val PARAM_STATE = "state"
    }
}
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
    HyperskillAnalyticRoute.Profile(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.DAILY_STUDY_REMINDS
) {
    companion object {
        private const val PARAM_STATE = "state"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(PARAM_CONTEXT to mapOf(PARAM_STATE to isEnabled))
}
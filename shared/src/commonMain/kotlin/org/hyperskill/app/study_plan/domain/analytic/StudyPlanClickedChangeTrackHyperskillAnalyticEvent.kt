package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click on "Change track" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "main",
 *     "target": "change_track"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
object StudyPlanClickedChangeTrackHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.CHANGE_TRACK
)
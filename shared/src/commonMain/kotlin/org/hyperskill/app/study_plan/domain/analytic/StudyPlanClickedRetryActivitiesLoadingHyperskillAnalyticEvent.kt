package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the error state placeholder in the section activities.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "study_plan_section_activities",
 *     "target": "retry",
 *     "context":
 *     {
 *         "section_id": 123
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent(
    val sectionId: Long
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STUDY_PLAN_SECTION_ACTIVITIES,
    target = HyperskillAnalyticTarget.RETRY,
    context = mapOf(SECTION_ID to sectionId)
) {
    companion object {
        private const val SECTION_ID = "section_id"
    }
}
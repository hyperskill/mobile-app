package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the ExpandCompleted button in the section.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "study_plan_section",
 *     "target": "expand_completed",
 *     "context":
 *     {
 *         "section_id": 123
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StudyPlanExpandCompletedActivitiesClickedHSAnalyticEvent(
    val sectionId: Long
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STUDY_PLAN_SECTION,
    target = HyperskillAnalyticTarget.EXPAND_COMPLETED,
    context = mapOf(StudyPlanAnalyticParams.PARAM_SECTION_ID to sectionId)
)
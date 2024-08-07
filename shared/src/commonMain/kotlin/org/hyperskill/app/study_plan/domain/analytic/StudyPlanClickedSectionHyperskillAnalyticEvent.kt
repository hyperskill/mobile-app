package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a click analytic event of the study plan section, when section is expanded or collapsed.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "study_plan_section",
 *     "target": "section",
 *     "context":
 *     {
 *         "section_id": 123,
 *         "is_expanded": true | false
 *     }
 * }
 * ```
 *
 * @property sectionId id of the section
 * @property isExpanded true if section is expanded, false if collapsed
 *
 * @see HyperskillAnalyticEvent
 */
class StudyPlanClickedSectionHyperskillAnalyticEvent(
    val sectionId: Long,
    val isExpanded: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STUDY_PLAN_SECTION,
    target = HyperskillAnalyticTarget.SECTION,
    context = mapOf(
        StudyPlanAnalyticParams.PARAM_SECTION_ID to sectionId,
        StudyPlanAnalyticParams.PARAM_IS_EXPANDED to isExpanded
    )
)
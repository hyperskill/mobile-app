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
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STUDY_PLAN_SECTION,
    HyperskillAnalyticTarget.SECTION
) {
    companion object {
        private const val SECTION_ID = "section_id"
        private const val IS_EXPANDED = "is_expanded"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    SECTION_ID to sectionId,
                    IS_EXPANDED to isExpanded
                )
            )
}
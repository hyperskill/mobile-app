package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.core.domain.model.ContentType
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents an analytic event for clicking on an activity in the study plan.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan"",
 *     "action": "click",
 *     "part": "study_plan_section_activities",
 *     "target": "activity",
 *     "context":
 *     {
 *         "id": 1,
 *         "type": 10,
 *         "target_type": "step",
 *         "target_id": 1
 *     }
 * }
 * ```
 *
 * @property activityId id of the activity
 * @property activityType type of the activity
 * @property activityTargetType type of the activity target
 * @property activityTargetId id of the activity target
 *
 * @see HyperskillAnalyticEvent
 */
class StudyPlanClickedActivityHyperskillAnalyticEvent(
    val activityId: Long,
    val activityType: Int?,
    val activityTargetType: ContentType?,
    val activityTargetId: Long?,
    val isLocked: Boolean
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.StudyPlan(),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.STUDY_PLAN_SECTION_ACTIVITIES,
    target = HyperskillAnalyticTarget.ACTIVITY,
    context = mapOfNotNull(
        ID to activityId,
        TYPE to activityType,
        TARGET_TYPE to activityTargetType,
        TARGET_ID to activityTargetId,
        IS_LOCKED to isLocked
    )
) {
    companion object {
        private const val ID = "id"
        private const val TYPE = "type"
        private const val TARGET_TYPE = "target_type"
        private const val TARGET_ID = "target_id"
        private const val IS_LOCKED = "is_locked"
    }
}
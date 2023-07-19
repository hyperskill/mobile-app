package org.hyperskill.app.next_learning_activity_widget.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents an analytic event for clicking on an activity in the next learning activity widget.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/home",
 *     "action": "click",
 *     "part": "next_learning_activity_widget",
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
internal class NextLearningActivityWidgetClickedHyperskillAnalyticEvent(
    private val activityId: Long,
    private val activityType: Int?,
    private val activityTargetType: String?,
    private val activityTargetId: Long?
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.NEXT_LEARNING_ACTIVITY_WIDGET,
    HyperskillAnalyticTarget.ACTIVITY
) {
    companion object {
        private const val ID = "id"
        private const val TYPE = "type"
        private const val TARGET_TYPE = "target_type"
        private const val TARGET_ID = "target_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOfNotNull(
                    ID to activityId,
                    TYPE to activityType,
                    TARGET_TYPE to activityTargetType,
                    TARGET_ID to activityTargetId
                )
            )
}
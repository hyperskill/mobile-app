package org.hyperskill.app.learning_activities.presentation.mapper

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.step.domain.model.StepRoute

internal class LearningActivityTargetViewActionMapperException(message: String) : Exception(message)

internal object LearningActivityTargetViewActionMapper {
    fun mapLearningActivityToTargetViewAction(
        activity: LearningActivity,
        trackId: Long?,
        projectId: Long?
    ): Result<LearningActivityTargetViewAction> =
        kotlin.runCatching {
            when (activity.type) {
                LearningActivityType.IMPLEMENT_STAGE -> {
                    if (projectId != null &&
                        activity.targetId != null && activity.targetType == LearningActivityTargetType.STAGE
                    ) {
                        if (activity.isIdeRequired) {
                            LearningActivityTargetViewAction.ShowStageImplementIDERequiredModal
                        } else {
                            LearningActivityTargetViewAction.NavigateTo.StageImplement(
                                stageId = activity.targetId,
                                projectId = projectId
                            )
                        }
                    } else {
                        throw LearningActivityTargetViewActionMapperException(
                            "IMPLEMENT_STAGE action failed for projectId = $projectId, activity = $activity"
                        )
                    }
                }
                LearningActivityType.LEARN_TOPIC -> {
                    if (activity.targetId != null && activity.targetType == LearningActivityTargetType.STEP) {
                        LearningActivityTargetViewAction.NavigateTo.Step(StepRoute.Learn.Step(activity.targetId))
                    } else {
                        throw LearningActivityTargetViewActionMapperException(
                            "LEARN_TOPIC action failed for activity = $activity"
                        )
                    }
                }
                LearningActivityType.SELECT_PROJECT -> {
                    if (trackId != null) {
                        LearningActivityTargetViewAction.NavigateTo.SelectProject(trackId = trackId)
                    } else {
                        throw LearningActivityTargetViewActionMapperException(
                            "SELECT_PROJECT action failed for trackId = $trackId, activity = $activity"
                        )
                    }
                }
                LearningActivityType.SELECT_TRACK -> {
                    LearningActivityTargetViewAction.NavigateTo.SelectTrack
                }
                else -> {
                    throw LearningActivityTargetViewActionMapperException(
                        "Unsupported action type = ${activity.typeValue}, activity = $activity"
                    )
                }
            }
        }
}
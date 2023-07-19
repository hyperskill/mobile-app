package org.hyperskill.app.study_plan.widget.presentation.mapper

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.widget.presentation.model.LearningActivityTargetViewAction

internal class LearningActivityTargetViewActionMapperException(message: String) : Exception(message)

internal object LearningActivityTargetViewActionMapper {
    fun mapLearningActivityToTargetViewAction(
        activity: LearningActivity,
        trackId: Long?,
        projectId: Long?
    ): Result<LearningActivityTargetViewAction> =
        when (activity.type) {
            LearningActivityType.IMPLEMENT_STAGE -> {
                if (projectId != null &&
                    activity.targetId != null && activity.targetType == LearningActivityTargetType.STAGE
                ) {
                    if (activity.isIdeRequired) {
                        Result.success(LearningActivityTargetViewAction.ShowStageImplementIDERequiredModal)
                    } else {
                        Result.success(
                            LearningActivityTargetViewAction.NavigateTo.StageImplement(
                                stageId = activity.targetId,
                                projectId = projectId
                            )
                        )
                    }
                } else {
                    Result.failure(
                        LearningActivityTargetViewActionMapperException(
                            "IMPLEMENT_STAGE action failed for projectId = $projectId, activity = $activity"
                        )
                    )
                }
            }
            LearningActivityType.LEARN_TOPIC -> {
                if (activity.targetId != null && activity.targetType == LearningActivityTargetType.STEP) {
                    Result.success(
                        LearningActivityTargetViewAction.NavigateTo.Step(StepRoute.Learn.Step(activity.targetId))
                    )
                } else {
                    Result.failure(
                        LearningActivityTargetViewActionMapperException(
                            "LEARN_TOPIC action failed for activity = $activity"
                        )
                    )
                }
            }
            LearningActivityType.SELECT_PROJECT -> {
                if (trackId != null) {
                    Result.success(LearningActivityTargetViewAction.NavigateTo.SelectProject(trackId = trackId))
                } else {
                    Result.failure(
                        LearningActivityTargetViewActionMapperException(
                            "SELECT_PROJECT action failed for trackId = $trackId, activity = $activity"
                        )
                    )
                }
            }
            LearningActivityType.SELECT_TRACK -> {
                Result.success(LearningActivityTargetViewAction.NavigateTo.SelectTrack)
            }
            else -> {
                Result.failure(
                    LearningActivityTargetViewActionMapperException(
                        "Unknown action type = ${activity.typeValue}, activity = $activity"
                    )
                )
            }
        }
}
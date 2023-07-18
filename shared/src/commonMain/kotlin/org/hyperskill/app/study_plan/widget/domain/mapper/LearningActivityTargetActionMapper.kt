package org.hyperskill.app.study_plan.widget.domain.mapper

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.widget.domain.model.LearningActivityTargetAction

internal object LearningActivityTargetActionMapper {
    fun mapLearningActivityToTargetAction(
        activity: LearningActivity,
        trackId: Long?,
        projectId: Long?
    ): LearningActivityTargetAction =
        when (activity.type) {
            LearningActivityType.IMPLEMENT_STAGE -> {
                if (projectId != null &&
                    activity.targetId != null && activity.targetType == LearningActivityTargetType.STAGE
                ) {
                    if (activity.isIdeRequired) {
                        LearningActivityTargetAction.StageImplement.IDERequired
                    } else {
                        LearningActivityTargetAction.StageImplement.Supported(
                            stageId = activity.targetId,
                            projectId = projectId
                        )
                    }
                } else {
                    LearningActivityTargetAction.StageImplement.Error
                }
            }
            LearningActivityType.LEARN_TOPIC -> {
                if (activity.targetId != null && activity.targetType == LearningActivityTargetType.STEP) {
                    LearningActivityTargetAction.LearnTopic.Supported(StepRoute.Learn.Step(activity.targetId))
                } else {
                    LearningActivityTargetAction.LearnTopic.Error
                }
            }
            LearningActivityType.SELECT_PROJECT -> {
                if (trackId != null) {
                    LearningActivityTargetAction.SelectProject.Supported(trackId = trackId)
                } else {
                    LearningActivityTargetAction.SelectProject.Error
                }
            }
            LearningActivityType.SELECT_TRACK -> {
                LearningActivityTargetAction.SelectTrack
            }
            else -> {
                LearningActivityTargetAction.Unsupported
            }
        }
}
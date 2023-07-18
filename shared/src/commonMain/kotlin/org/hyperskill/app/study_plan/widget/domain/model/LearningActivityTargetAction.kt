package org.hyperskill.app.study_plan.widget.domain.model

import org.hyperskill.app.step.domain.model.StepRoute

sealed interface LearningActivityTargetAction {
    sealed interface StageImplement : LearningActivityTargetAction {
        object Error : StageImplement
        object IDERequired : StageImplement
        data class Supported(val stageId: Long, val projectId: Long) : StageImplement
    }

    sealed interface LearnTopic : LearningActivityTargetAction {
        object Error : LearnTopic
        data class Supported(val stepRoute: StepRoute) : LearnTopic
    }

    sealed interface SelectProject : LearningActivityTargetAction {
        object Error : SelectProject
        data class Supported(val trackId: Long) : SelectProject
    }

    object SelectTrack : LearningActivityTargetAction

    object Unsupported : LearningActivityTargetAction
}
package org.hyperskill.app.learning_activities.presentation.model

import org.hyperskill.app.step.domain.model.StepRoute

sealed interface LearningActivityTargetViewAction {
    object ShowStageImplementIDERequiredModal : LearningActivityTargetViewAction

    sealed interface NavigateTo : LearningActivityTargetViewAction {
        data class Step(val stepRoute: StepRoute) : NavigateTo

        data class StageImplement(val stageId: Long, val projectId: Long) : NavigateTo

        object SelectTrack : NavigateTo
        data class SelectProject(val trackId: Long) : NavigateTo
    }
}
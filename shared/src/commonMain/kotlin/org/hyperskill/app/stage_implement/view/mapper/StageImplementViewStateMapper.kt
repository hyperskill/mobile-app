package org.hyperskill.app.stage_implement.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.step.domain.model.StepRoute

internal class StageImplementViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapState(state: StageImplementFeature.State): StageImplementFeature.ViewState =
        when (state) {
            is StageImplementFeature.State.Idle ->
                StageImplementFeature.ViewState.Idle
            is StageImplementFeature.State.Loading ->
                StageImplementFeature.ViewState.Loading
            is StageImplementFeature.State.Deprecated ->
                StageImplementFeature.ViewState.Deprecated(
                    message = resourceProvider.getString(
                        SharedResources.strings.stage_implement_project_deprecated_text,
                        state.project.title
                    ),
                    ctaButtonText = resourceProvider.getString(SharedResources.strings.back)
                )
            is StageImplementFeature.State.Unsupported ->
                StageImplementFeature.ViewState.Unsupported
            is StageImplementFeature.State.NetworkError ->
                StageImplementFeature.ViewState.NetworkError
            is StageImplementFeature.State.Content ->
                StageImplementFeature.ViewState.Content(
                    stepRoute = StepRoute.StageImplement(
                        stepId = state.step.id,
                        projectId = state.project.id,
                        stageId = state.stage.id
                    ),
                    navigationTitle = resourceProvider.getString(
                        SharedResources.strings.stage_implement_title,
                        state.stage.stepIndex,
                        state.stage.projectStagesCount
                    ),
                    stageTitle = state.stage.title
                )
        }
}
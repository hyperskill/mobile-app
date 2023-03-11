package org.hyperskill.app.debug.view

import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugFeature.ViewState.Content.NavigationInput

internal object DebugViewStateMapper {
    fun mapState(state: DebugFeature.State): DebugFeature.ViewState =
        when (state) {
            is DebugFeature.State.Idle -> DebugFeature.ViewState.Idle
            is DebugFeature.State.Loading -> DebugFeature.ViewState.Loading
            is DebugFeature.State.Error -> DebugFeature.ViewState.Error
            is DebugFeature.State.Content -> {
                DebugFeature.ViewState.Content(
                    availableEndpointConfigs = EndpointConfigType.values().toList(),
                    selectedEndpointConfig = state.selectedEndpointConfig,
                    isApplySettingsButtonAvailable = state.selectedEndpointConfig != state.currentEndpointConfig,
                    navigationInput = NavigationInput(
                        step = NavigationInput.Step(
                            stepId = state.navigationInput.stepId,
                            isOpenEnabled = state.navigationInput.stepId.isNotBlank()
                        ),
                        stageImplement = NavigationInput.StageImplement(
                            projectId = state.navigationInput.stageImplement.projectId,
                            stageId = state.navigationInput.stageImplement.stageId,
                            isOpenEnabled = state.navigationInput.stageImplement.projectId.isNotBlank() &&
                                state.navigationInput.stageImplement.stageId.isNotBlank()
                        )
                    )
                )
            }
        }
}
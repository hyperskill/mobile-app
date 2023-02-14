package org.hyperskill.app.debug.view

import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.presentation.DebugFeature

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
                    stepNavigationInputText = state.stepNavigationInputText,
                    isStepNavigationOpenButtonEnabled = state.stepNavigationInputText.isNotBlank(),
                    isApplySettingsButtonAvailable = state.selectedEndpointConfig != state.currentEndpointConfig
                )
            }
        }
}
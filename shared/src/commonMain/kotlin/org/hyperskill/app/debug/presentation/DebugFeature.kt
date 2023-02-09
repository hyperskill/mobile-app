package org.hyperskill.app.debug.presentation

import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.debug.domain.model.DebugSettings
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.step.domain.model.StepRoute

object DebugFeature {
    fun isAvailable(buildKonfig: BuildKonfig): Boolean =
        BuildKonfig.IS_INTERNAL_TESTING ?: (buildKonfig.buildVariant == BuildVariant.DEBUG)

    internal sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val currentEndpointConfigType: EndpointConfigType,
            val selectedEndpointConfigType: EndpointConfigType,
            val stepNavigationInputText: String
        ) : State
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val availableEndpointConfigTypes: List<EndpointConfigType>,
            val selectedEndpointConfigType: EndpointConfigType,
            val stepNavigationInputText: String,
            val isStepNavigationOpenButtonEnabled: Boolean,
            val isApplySettingsButtonAvailable: Boolean
        ) : ViewState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        data class FetchDebugSettingsSuccess(val debugSettings: DebugSettings) : Message
        object FetchDebugSettingsFailure : Message

        data class SelectEndpointConfig(val endpointConfigType: EndpointConfigType) : Message

        /**
         * Step navigation
         */
        data class StepNavigationInputTextChanged(val text: String) : Message
        object StepNavigationOpenClicked : Message

        /**
         * Click on apply button
         */
        object ApplySettingsClicked : Message
        object ApplySettingsSuccess : Message
        object ApplySettingsFailure : Message
    }

    sealed interface Action {
        object FetchDebugSettings : Action
        data class UpdateEndpointConfig(val endpointConfigType: EndpointConfigType) : Action

        sealed interface ViewAction : Action {
            object RestartApplication : ViewAction
            data class OpenStep(val stepRoute: StepRoute) : ViewAction
        }
    }
}
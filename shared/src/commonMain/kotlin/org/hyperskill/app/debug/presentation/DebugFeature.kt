package org.hyperskill.app.debug.presentation

import org.hyperskill.app.debug.domain.model.DebugSettings
import org.hyperskill.app.debug.domain.model.EndpointConfigType

interface DebugFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(val selectedEndpointConfigType: EndpointConfigType) : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        data class FetchDebugSettingsSuccess(val debugSettings: DebugSettings) : Message
        object FetchDebugSettingsFailure : Message

        data class SelectEndpointConfig(val endpointConfigType: EndpointConfigType) : Message

        /**
         * Click on apply button
         */
        object ApplySettings : Message
        object ApplySettingsSuccess : Message
        object ApplySettingsFailure : Message
    }

    sealed interface Action {
        object FetchDebugSettings : Action
        data class UpdateEndpointConfig(val endpointConfigType: EndpointConfigType) : Action

        sealed interface ViewAction : Action {
            object RestartApplication : ViewAction
        }
    }
}
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
            val currentEndpointConfig: EndpointConfigType,
            val selectedEndpointConfig: EndpointConfigType,
            val navigationInput: NavigationInput = NavigationInput()
        ) : State {
            data class NavigationInput(
                val stepId: String = "",
                val stageImplement: StageImplement = StageImplement()
            ) {
                data class StageImplement(val projectId: String = "", val stageId: String = "")
            }
        }
    }

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val availableEndpointConfigs: List<EndpointConfigType>,
            val selectedEndpointConfig: EndpointConfigType,
            val isApplySettingsButtonAvailable: Boolean,
            val navigationInput: NavigationInput
        ) : ViewState {
            data class NavigationInput(
                val step: Step,
                val stageImplement: StageImplement
            ) {
                data class Step(val stepId: String, val isOpenEnabled: Boolean)
                data class StageImplement(val projectId: String, val stageId: String, val isOpenEnabled: Boolean)
            }
        }
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message
        data class FetchDebugSettingsSuccess(val debugSettings: DebugSettings) : Message
        object FetchDebugSettingsFailure : Message

        data class SelectEndpointConfig(val endpointConfig: EndpointConfigType) : Message

        /**
         * Step navigation
         */
        data class StepNavigationInputTextChanged(val text: String) : Message
        object StepNavigationOpenClicked : Message

        /**
         * Stage implement navigation
         */
        data class StageImplementNavigationInputChanged(val projectId: String, val stageId: String) : Message
        object StageImplementNavigationOpenClicked : Message

        /**
         * Click on apply button
         */
        object ApplySettingsClicked : Message
        object ApplySettingsSuccess : Message
        object ApplySettingsFailure : Message
    }

    sealed interface Action {
        object FetchDebugSettings : Action
        data class UpdateEndpointConfig(val endpointConfig: EndpointConfigType) : Action

        sealed interface ViewAction : Action {
            object RestartApplication : ViewAction
            data class OpenStep(val stepRoute: StepRoute) : ViewAction
            data class OpenStageImplement(val projectId: Long, val stageId: Long) : ViewAction
        }
    }
}
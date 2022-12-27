package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.Step

interface StepFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Data(val step: Step) : State
    }

    sealed interface Message {
        data class Initialize(
            val stepRoute: StepRoute,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface StepLoaded : Message {
            data class Success(val step: Step) : StepLoaded
            object Error : StepLoaded
        }

        object ClickedBackEventMessage : Message
    }

    sealed interface Action {
        data class FetchStep(val stepRoute: StepRoute) : Action
        sealed class ViewAction : Action
        data class LogClickedBackEvent(val stepRoute: StepRoute) : Action
    }
}
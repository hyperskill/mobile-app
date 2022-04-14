package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.model.Step

interface StepFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Data(val step: Step) : State
    }

    sealed interface Message {
        data class Init(
            val stepId: Long,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface StepLoaded : Message {
            data class Success(val step: Step) : StepLoaded
            data class Error(val errorMsg: String) : StepLoaded
        }
    }

    sealed interface Action {
        data class FetchStep(val stepId: Long) : Action
        sealed class ViewAction : Action
    }
}
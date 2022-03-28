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
    }

    sealed interface Action {
        sealed class ViewAction : Action
    }
}
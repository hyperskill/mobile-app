package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute

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

        data class ViewedEventMessage(val stepRoute: StepRoute) : Message
    }

    sealed interface Action {
        data class FetchStep(val stepRoute: StepRoute) : Action
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
        sealed class ViewAction : Action
    }
}
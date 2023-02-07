package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature

interface StepFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Data(
            val step: Step,
            val isPracticingAvailable: Boolean,
            val stepCompletionState: StepCompletionFeature.State
        ) : State
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        sealed interface StepLoaded : Message {
            data class Success(val step: Step) : StepLoaded
            object Error : StepLoaded
        }

        /**
         * Analytic
         */
        object ViewedEventMessage : Message

        /**
         * Message Wrappers
         */
        data class StepCompletionMessage(val message: StepCompletionFeature.Message) : Message
    }

    sealed interface Action {
        data class FetchStep(val stepRoute: StepRoute) : Action
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action
        data class ViewStep(val stepId: Long, val stepContext: StepContext) : Action

        /**
         * Action Wrappers
         */
        data class StepCompletionAction(val action: StepCompletionFeature.Action) : Action

        sealed interface ViewAction : Action {
            data class StepCompletionViewAction(
                val viewAction: StepCompletionFeature.Action.ViewAction
            ) : ViewAction
        }
    }
}
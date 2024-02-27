package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature

object StepFeature {
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
        sealed interface ViewAction : Action {
            data class StepCompletionViewAction(
                val viewAction: StepCompletionFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchStep(val stepRoute: StepRoute) : InternalAction
        data class ViewStep(val stepId: Long, val stepContext: StepContext) : InternalAction

        data class UpdateNextLearningActivityState(val step: Step) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        /**
         * Action Wrappers
         */
        data class StepCompletionAction(val action: StepCompletionFeature.Action) : InternalAction
    }
}
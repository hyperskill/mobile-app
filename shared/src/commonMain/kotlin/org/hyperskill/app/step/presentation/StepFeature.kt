package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature

object StepFeature {
    data class State(
        val stepState: StepState,
        val stepToolbarState: StepToolbarFeature.State
    )

    internal fun initialState() =
        State(StepState.Idle, StepToolbarFeature.State.Idle)

    data class ViewState(
        val stepState: StepState,
        val stepToolbarViewState: StepToolbarFeature.ViewState
    )

    sealed interface StepState {
        object Idle : StepState
        object Loading : StepState
        object Error : StepState
        data class Data(
            val step: Step,
            val isPracticingAvailable: Boolean,
            val stepCompletionState: StepCompletionFeature.State
        ) : StepState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean = false) : Message

        sealed interface StepLoaded : Message {
            data class Success(val step: Step) : StepLoaded
            object Error : StepLoaded
        }

        object ScreenShowed : Message
        object ScreenHidden : Message

        /**
         * Message Wrappers
         */
        data class StepCompletionMessage(val message: StepCompletionFeature.Message) : Message

        data class StepToolbarMessage(val message: StepToolbarFeature.Message) : Message
    }

    internal sealed interface InternalMessage : Message {
        data class StepCompleted(val stepId: Long) : InternalMessage

        object SolvingTimerFired : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class StepCompletionViewAction(
                val viewAction: StepCompletionFeature.Action.ViewAction
            ) : ViewAction

            data class StepToolbarViewAction(
                val viewAction: StepToolbarFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchStep(val stepRoute: StepRoute) : InternalAction

        data class ViewStep(val stepId: Long, val stepContext: StepContext) : InternalAction

        object StartSolvingTimer : InternalAction
        object StopSolvingTimer : InternalAction
        data class LogSolvingTime(val stepId: Long) : InternalAction

        data class UpdateNextLearningActivityState(val step: Step) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        /**
         * Action Wrappers
         */
        data class StepCompletionAction(val action: StepCompletionFeature.Action) : InternalAction

        data class StepToolbarAction(val action: StepToolbarFeature.Action) : InternalAction
    }
}
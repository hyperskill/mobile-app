package org.hyperskill.app.interview_preparation.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.StepRoute

object InterviewPreparationWidgetFeature {
    sealed interface State {
        object Idle : State
        data class Loading(val isLoadingSilently: Boolean) : State
        object Error : State
        data class Content(
            val stepsCount: Int,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface Message {
        object ViewedEventMessage : Message
        object RetryContentLoading : Message
        object WidgetClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        /**
         * The success result for [InternalAction.FetchInterviewSteps]
         */
        data class FetchInterviewStepsResultSuccess(val steps: List<Long>) : InternalMessage
        /**
         * The error result for [InternalAction.FetchInterviewSteps]
         */
        object FetchInterviewStepsResultError : InternalMessage

        object PullToRefresh : InternalMessage

        data class StepsCountChanged(val stepsCount: Int) : InternalMessage

        /**
         * The success result for [InternalAction.FetchNextInterviewStep]
         */
        data class FetchNextInterviewStepResultSuccess(val stepId: Long) : InternalMessage
        /**
         * The error result for [InternalAction.FetchNextInterviewStep]
         */
        data class FetchNextInterviewStepResultError(val errorMessage: String) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowOpenStepError(val errorMessage: String) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class Step(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction

        /**
         * Fetch interview steps.
         * The result of this action is
         * [InternalMessage.FetchInterviewStepsResultSuccess] or [InternalMessage.FetchInterviewStepsResultError]
         */
        data class FetchInterviewSteps(val forceLoadFromNetwork: Boolean) : InternalAction

        /**
         * Fetch next interview step id.
         * The result of this action is
         * [InternalMessage.FetchNextInterviewStepResultSuccess] or [InternalMessage.FetchNextInterviewStepResultError]
         */
        object FetchNextInterviewStep : InternalAction
    }
}
package org.hyperskill.app.interview_preparation.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.StepId
import org.hyperskill.app.step.domain.model.StepRoute

object InterviewPreparationWidgetFeature {
    sealed interface State {
        object Idle : State
        data class Loading(val isLoadingSilently: Boolean) : State
        object Error : State
        data class Content(
            val steps: List<StepId>,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    sealed interface Message {
        object RetryContentLoading : Message
        object WidgetClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        /**
         * The result for [InternalAction.FetchInterviewSteps]
         */
        sealed interface FetchInterviewStepsResult : InternalMessage {
            data class Success(val steps: List<StepId>) : FetchInterviewStepsResult
            object Error : FetchInterviewStepsResult
        }

        object PullToRefresh : InternalMessage

        data class StepSolved(val stepId: StepId) : InternalMessage
    }
    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class Step(val stepRoute: StepRoute) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val event: AnalyticEvent) : InternalAction

        /**
         * Fetch interview steps.
         * The result of this action is [InternalMessage.FetchInterviewStepsResult].
         */
        object FetchInterviewSteps : InternalAction
    }
}
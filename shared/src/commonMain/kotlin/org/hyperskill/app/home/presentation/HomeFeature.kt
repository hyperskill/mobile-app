package org.hyperskill.app.home.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.streak.domain.model.Streak

interface HomeFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        data class Content(val streak: Streak?, val problemOfDayState: ProblemOfDayState) : State
        object NetworkError : State
    }

    sealed interface ProblemOfDayState {
        object Empty : ProblemOfDayState
        data class NeedToSolve(val step: Step, val nextProblemIn: Long) : ProblemOfDayState
        data class Solved(val step: Step, val nextProblemIn: Long) : ProblemOfDayState
    }

    sealed interface Message {
        data class Init(val forceUpdate: Boolean) : Message
        data class HomeSuccess(val streak: Streak?, val problemOfDayState: ProblemOfDayState) : Message
        data class HomeNextProblemInUpdate(val seconds: Long) : Message
        object ReadyToLaunchNextProblemInTimer : Message
        object HomeFailure : Message
        data class ProblemOfDaySolved(val stepId: Long) : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedProblemOfDayCardEventMessage : Message
        object ClickedContinueLearningOnWebEventMessage : Message
    }

    sealed interface Action {
        object FetchHomeScreenData : Action
        object LaunchTimer : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepId: Long) : NavigateTo
                object TopicsRepetitions : NavigateTo
            }
        }
    }
}
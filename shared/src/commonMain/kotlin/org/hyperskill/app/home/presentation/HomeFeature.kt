package org.hyperskill.app.home.presentation

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
        data class NeedToSolve(val step: Step) : ProblemOfDayState
        data class Solved(val step: Step) : ProblemOfDayState
    }

    sealed interface Message {
        data class Init(val forceUpdate: Boolean) : Message
        data class HomeSuccess(val streak: Streak?, val problemOfDayState: ProblemOfDayState): Message
        object HomeFailure: Message
    }

    sealed interface Action {
        object FetchHomeScreenData: Action
        sealed class ViewAction : Action
    }
}
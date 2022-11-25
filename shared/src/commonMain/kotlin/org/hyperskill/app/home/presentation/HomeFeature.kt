package org.hyperskill.app.home.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.streak.domain.model.Streak

interface HomeFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading home screen data.
         */
        object Loading : State

        /**
         * Represents a state when home screen data successfully loaded.
         *
         * @property streak Current user profile streak.
         * @property problemOfDayState Problem of the day state.
         * @property recommendedRepetitionsCount Recommended topics repetitions count.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @property isLoadingMagicLink A boolean flag that indicates about magic link loading.
         * @see Streak
         * @see ProblemOfDayState
         */
        data class Content(
            val streak: Streak?,
            val problemOfDayState: ProblemOfDayState,
            val recommendedRepetitionsCount: Int,
            val isRefreshing: Boolean = false,
            val isLoadingMagicLink: Boolean = false
        ) : State

        /**
         * Represents a state when home screen data failed to load.
         */
        object NetworkError : State
    }

    sealed interface ProblemOfDayState {
        object Empty : ProblemOfDayState
        data class NeedToSolve(val step: Step, val nextProblemIn: Long) : ProblemOfDayState
        data class Solved(val step: Step, val nextProblemIn: Long) : ProblemOfDayState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message
        data class HomeSuccess(
            val streak: Streak?,
            val problemOfDayState: ProblemOfDayState,
            val recommendedRepetitionsCount: Int
        ) : Message
        object HomeFailure : Message
        object PullToRefresh : Message

        object ReadyToLaunchNextProblemInTimer : Message
        data class HomeNextProblemInUpdate(val seconds: Long) : Message

        data class ProblemOfDaySolved(val stepId: Long) : Message
        object TopicRepeated : Message

        object ClickedContinueLearningOnWeb : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedProblemOfDayCardEventMessage : Message
        object ClickedTopicsRepetitionsCardEventMessage : Message
        object ClickedContinueLearningOnWebEventMessage : Message
    }

    sealed interface Action {
        object FetchHomeScreenData : Action
        object LaunchTimer : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepId: Long) : NavigateTo
                object TopicsRepetitionsScreen : NavigateTo
            }
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction
        }
    }
}
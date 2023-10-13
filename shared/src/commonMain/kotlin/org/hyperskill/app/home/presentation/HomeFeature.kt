package org.hyperskill.app.home.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.streaks.domain.model.Streak

interface HomeFeature {
    data class State(
        val homeState: HomeState,
        val toolbarState: GamificationToolbarFeature.State
    ) {
        val isRefreshing: Boolean
            get() = homeState is HomeState.Content && homeState.isRefreshing ||
                toolbarState.isRefreshing
    }

    sealed interface HomeState {
        /**
         * Represents initial state.
         */
        object Idle : HomeState

        /**
         * Represents a state when loading home screen data.
         */
        object Loading : HomeState

        /**
         * Represents a state when home screen data successfully loaded.
         *
         * @property problemOfDayState Problem of the day state.
         * @property repetitionsState Topics repetitions state.
         * @property isFreemiumEnabled A boolean flag that indicates about is freemium enabled.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         *
         * @see Streak
         * @see ProblemOfDayState
         */
        data class Content(
            val problemOfDayState: ProblemOfDayState,
            val repetitionsState: RepetitionsState,
            val isFreemiumEnabled: Boolean,
            internal val isRefreshing: Boolean = false
        ) : HomeState

        /**
         * Represents a state when home screen data failed to load.
         */
        object NetworkError : HomeState
    }

    sealed interface ProblemOfDayState {
        /**
         * Represents state when problem of day is unavailable
         */
        object Empty : ProblemOfDayState

        /**
         * Represents state when problem of day is available and not solved
         * @property step Daily step to be solved
         * @property nextProblemIn Daily step updating in formatted time
         * @property needToRefresh Indicates that reload button should be shown in daily problem card
         */
        data class NeedToSolve(
            val step: Step,
            val nextProblemIn: String,
            val needToRefresh: Boolean = false
        ) : ProblemOfDayState

        /**
         * Represents state when problem of day is available and solved
         * @property step Daily step to be solved
         * @property nextProblemIn Daily step updating in formatted time
         * @property needToRefresh Indicates that reload button should be shown in daily problem card
         */
        data class Solved(
            val step: Step,
            val nextProblemIn: String,
            val needToRefresh: Boolean = false
        ) : ProblemOfDayState
    }

    sealed interface RepetitionsState {
        object Empty : RepetitionsState
        data class Available(val recommendedRepetitionsCount: Int) : RepetitionsState
    }

    sealed interface Message {
        data class Initialize(val forceUpdate: Boolean) : Message
        data class HomeSuccess(
            val problemOfDayState: ProblemOfDayState,
            val repetitionsState: RepetitionsState,
            val isFreemiumEnabled: Boolean
        ) : Message

        object HomeFailure : Message
        object PullToRefresh : Message

        object ReadyToLaunchNextProblemInTimer : Message
        object NextProblemInTimerStopped : Message
        data class HomeNextProblemInUpdate(val nextProblemIn: String) : Message

        data class StepQuizSolved(val stepId: Long) : Message
        object TopicRepeated : Message

        object ClickedTopicsRepetitionsCard : Message
        object ClickedProblemOfDayCardReload : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedProblemOfDayCardEventMessage : Message

        /**
         * TODO: ALTAPPS-908 refactor this
         * Stage implementation unsupported modal
         */
        object StageImplementUnsupportedModalGoToHomeClicked : Message
        object StageImplementUnsupportedModalShownEventMessage : Message
        object StageImplementUnsupportedModalHiddenEventMessage : Message

        /**
         * Message Wrappers
         */
        data class GamificationToolbarMessage(val message: GamificationToolbarFeature.Message) : Message
    }

    sealed interface Action {
        object FetchHomeScreenData : Action
        object LaunchTimer : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        /**
         * Action Wrappers
         */
        data class GamificationToolbarAction(val action: GamificationToolbarFeature.Action) : Action

        sealed interface ViewAction : Action {

            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction

            sealed interface NavigateTo : ViewAction {
                data class StepScreen(val stepRoute: StepRoute) : NavigateTo
                object TopicsRepetitionsScreen : NavigateTo
            }
        }
    }
}
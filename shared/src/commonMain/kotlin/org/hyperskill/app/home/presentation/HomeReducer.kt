package org.hyperskill.app.home.presentation

import org.hyperskill.app.home.domain.analytic.*
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class HomeReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.NetworkError))
                ) {
                    State.Loading to setOf(Action.FetchHomeScreenData)
                } else {
                    null
                }
            is Message.HomeSuccess ->
                State.Content(message.streak, message.problemOfDayState, message.recommendedRepetitionsCount) to emptySet()
            is Message.HomeFailure ->
                State.NetworkError to emptySet()
            is Message.PullToRefresh ->
                if (state is State.Content && !state.isRefreshing) {
                    state.copy(isRefreshing = true) to setOf(
                        Action.FetchHomeScreenData,
                        Action.LogAnalyticEvent(HomeClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            is Message.HomeNextProblemInUpdate ->
                if (state is State.Content) {
                    when (state.problemOfDayState) {
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            state.copy(
                                problemOfDayState = HomeFeature.ProblemOfDayState.NeedToSolve(
                                    state.problemOfDayState.step,
                                    message.seconds
                                )
                            ) to emptySet()
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            state.copy(
                                problemOfDayState = HomeFeature.ProblemOfDayState.Solved(
                                    state.problemOfDayState.step,
                                    message.seconds
                                )
                            ) to emptySet()
                        }
                        else -> {
                            null
                        }
                    }
                } else {
                    null
                }
            is Message.ReadyToLaunchNextProblemInTimer ->
                if (state is State.Content) {
                    state to setOf(Action.LaunchTimer)
                } else {
                    null
                }
            is Message.ProblemOfDaySolved -> {
                if (
                    state is State.Content &&
                    state.problemOfDayState is HomeFeature.ProblemOfDayState.NeedToSolve &&
                    state.problemOfDayState.step.id == message.stepId
                ) {
                    val completedStep = state.problemOfDayState.step.copy(isCompleted = true)
                    val updatedStreak = state.streak?.getStreakWithTodaySolved()

                    state.copy(
                        streak = updatedStreak,
                        problemOfDayState = HomeFeature.ProblemOfDayState.Solved(
                            completedStep,
                            HomeActionDispatcher.calculateNextProblemIn()
                        )
                    ) to setOf()
                } else {
                    null
                }
            }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(HomeViewedHyperskillAnalyticEvent()))
            is Message.ClickedProblemOfDayCardEventMessage -> {
                if (state is State.Content) {
                    when (state.problemOfDayState) {
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            state to setOf(
                                Action.LogAnalyticEvent(
                                    HomeClickedProblemOfDayCardHyperskillAnalyticEvent(
                                        isCompleted = false
                                    )
                                )
                            )
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            state to setOf(
                                Action.LogAnalyticEvent(
                                    HomeClickedProblemOfDayCardHyperskillAnalyticEvent(
                                        isCompleted = true
                                    )
                                )
                            )
                        }
                        else -> {
                            null
                        }
                    }
                } else {
                    null
                }
            }
            is Message.ClickedTopicsRepetitionsCardEventMessage ->
                if (state is State.Content) {
                    state to setOf(
                        Action.LogAnalyticEvent(
                            HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(
                                isCompleted = state.recommendedRepetitionsCount == 0
                            )
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedContinueLearningOnWebEventMessage ->
                state to setOf(Action.LogAnalyticEvent(HomeClickedContinueLearningOnWebHyperskillAnalyticEvent()))
        } ?: (state to emptySet())
}
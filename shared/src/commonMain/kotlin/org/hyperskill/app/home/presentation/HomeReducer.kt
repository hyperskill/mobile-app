package org.hyperskill.app.home.presentation

import kotlin.math.max
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.home.domain.analytic.HomeClickedContinueLearningOnWebHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedGemsBarButtonItemHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedStreakBarButtonItemHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeViewedHyperskillAnalyticEvent
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
                State.Content(
                    message.streak,
                    message.hypercoinsBalance,
                    message.problemOfDayState,
                    message.recommendedRepetitionsCount
                ) to emptySet()
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
            // Timer Messages
            is Message.ReadyToLaunchNextProblemInTimer ->
                if (state is State.Content) {
                    state to setOf(Action.LaunchTimer)
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
            // Flow Messages
            is Message.StepQuizSolved -> {
                if (state is State.Content) {
                    val problemOfDayState = if (
                        state.problemOfDayState is HomeFeature.ProblemOfDayState.NeedToSolve &&
                        state.problemOfDayState.step.id == message.stepId
                    ) {
                        HomeFeature.ProblemOfDayState.Solved(
                            state.problemOfDayState.step.copy(isCompleted = true),
                            HomeActionDispatcher.calculateNextProblemIn()
                        )
                    } else {
                        state.problemOfDayState
                    }

                    state.copy(
                        streak = state.streak?.getStreakWithTodaySolved(),
                        problemOfDayState = problemOfDayState
                    ) to emptySet()
                } else {
                    null
                }
            }
            is Message.TopicRepeated ->
                if (state is State.Content) {
                    state.copy(
                        recommendedRepetitionsCount = max(state.recommendedRepetitionsCount.dec(), 0)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.HypercoinsBalanceChanged ->
                if (state is State.Content) {
                    state.copy(hypercoinsBalance = message.hypercoinsBalance) to emptySet()
                } else {
                    null
                }
            // Click Messages
            is Message.ClickedContinueLearningOnWeb -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = true) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.Index()),
                        Action.LogAnalyticEvent(HomeClickedContinueLearningOnWebHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            }
            is Message.ClickedTopicsRepetitionsCard ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.TopicsRepetitionsScreen(state.recommendedRepetitionsCount),
                        Action.LogAnalyticEvent(
                            HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(
                                isCompleted = state.recommendedRepetitionsCount == 0
                            )
                        )
                    )
                } else {
                    null
                }
            Message.ClickedGemsBarButtonItem ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.ProfileTab,
                        Action.LogAnalyticEvent(HomeClickedGemsBarButtonItemHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            Message.ClickedStreakBarButtonItem ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.ProfileTab,
                        Action.LogAnalyticEvent(HomeClickedStreakBarButtonItemHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            // MagicLinks Messages
            is Message.GetMagicLinkReceiveSuccess -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.OpenUrl(message.url))
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveFailure -> {
                if (state is State.Content) {
                    state.copy(isLoadingMagicLink = false) to setOf(Action.ViewAction.ShowGetMagicLinkError)
                } else {
                    null
                }
            }
            // Analytic Messages
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(HomeViewedHyperskillAnalyticEvent()))
            is Message.ClickedContinueLearningOnWebEventMessage ->
                state to setOf(Action.LogAnalyticEvent(HomeClickedContinueLearningOnWebHyperskillAnalyticEvent()))
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
        } ?: (state to emptySet())
}
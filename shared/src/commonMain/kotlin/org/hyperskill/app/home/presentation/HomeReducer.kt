package org.hyperskill.app.home.presentation

import kotlin.math.max
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.domain.analytic.HomeClickedContinueLearningOnWebHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeViewedHyperskillAnalyticEvent
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.HomeState
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

class HomeReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer,
    private val topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                val (homeState, homeActions) = if (state.homeState is HomeState.Idle ||
                    (message.forceUpdate && (state.homeState is HomeState.Content || state.homeState is HomeState.NetworkError))
                ) {
                    HomeState.Loading to setOf(Action.FetchHomeScreenData)
                } else {
                    state.homeState to emptySet()
                }

                val (toolbarState, toolbarActions) = reduceGamificationToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.Initialize(GamificationToolbarScreen.HOME, message.forceUpdate)
                )

                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) = reduceTopicsToDiscoverNextMessage(
                    state.topicsToDiscoverNextState,
                    TopicsToDiscoverNextFeature.Message.Initialize(message.forceUpdate)
                )

                state.copy(
                    homeState = homeState,
                    toolbarState = toolbarState,
                    topicsToDiscoverNextState = topicsToDiscoverNextState
                ) to homeActions + toolbarActions + topicsToDiscoverNextActions
            }
            is Message.HomeSuccess ->
                state.copy(
                    homeState = HomeState.Content(message.problemOfDayState, message.repetitionsState)
                ) to emptySet()
            is Message.HomeFailure ->
                state.copy(homeState = HomeState.NetworkError) to emptySet()
            is Message.PullToRefresh -> {
                val (homeState, homeActions) = if (
                    state.homeState is HomeState.Content && !state.homeState.isRefreshing
                ) {
                    state.homeState.copy(isRefreshing = true) to setOf(
                        Action.FetchHomeScreenData,
                        Action.LogAnalyticEvent(HomeClickedPullToRefreshHyperskillAnalyticEvent())
                    )
                } else {
                    state.homeState to emptySet()
                }

                val (toolbarState, toolbarActions) = reduceGamificationToolbarMessage(
                    state.toolbarState,
                    GamificationToolbarFeature.Message.PullToRefresh(GamificationToolbarScreen.HOME)
                )

                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) = reduceTopicsToDiscoverNextMessage(
                    state.topicsToDiscoverNextState,
                    TopicsToDiscoverNextFeature.Message.PullToRefresh
                )

                state.copy(
                    homeState = homeState,
                    toolbarState = toolbarState,
                    topicsToDiscoverNextState = topicsToDiscoverNextState
                ) to homeActions + toolbarActions + topicsToDiscoverNextActions
            }
            // Timer Messages
            is Message.ReadyToLaunchNextProblemInTimer ->
                if (state.homeState is HomeState.Content) {
                    state to setOf(Action.LaunchTimer)
                } else {
                    null
                }
            is Message.NextProblemInTimerStopped ->
                if (state.homeState is HomeState.Content) {
                    when (state.homeState.problemOfDayState) {
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            state.copy(
                                homeState = state.homeState.copy(
                                    problemOfDayState = state.homeState.problemOfDayState.copy(needToRefresh = true)
                                )
                            ) to emptySet()
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            state.copy(
                                homeState = state.homeState.copy(
                                    problemOfDayState = state.homeState.problemOfDayState.copy(needToRefresh = true)
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
            is Message.HomeNextProblemInUpdate ->
                if (state.homeState is HomeState.Content) {
                    when (state.homeState.problemOfDayState) {
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            state.copy(
                                homeState = state.homeState.copy(
                                    problemOfDayState = HomeFeature.ProblemOfDayState.NeedToSolve(
                                        state.homeState.problemOfDayState.step,
                                        message.nextProblemIn
                                    )
                                )
                            ) to emptySet()
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            state.copy(
                                homeState = state.homeState.copy(
                                    problemOfDayState = HomeFeature.ProblemOfDayState.Solved(
                                        state.homeState.problemOfDayState.step,
                                        message.nextProblemIn
                                    )
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
                if (state.homeState is HomeState.Content) {
                    val problemOfDayState = if (
                        state.homeState.problemOfDayState is HomeFeature.ProblemOfDayState.NeedToSolve &&
                        state.homeState.problemOfDayState.step.id == message.stepId
                    ) {
                        HomeFeature.ProblemOfDayState.Solved(
                            state.homeState.problemOfDayState.step.copy(isCompleted = true),
                            state.homeState.problemOfDayState.nextProblemIn
                        )
                    } else {
                        state.homeState.problemOfDayState
                    }

                    state.copy(homeState = state.homeState.copy(problemOfDayState = problemOfDayState)) to emptySet()
                } else {
                    null
                }
            }
            is Message.TopicRepeated ->
                if (
                    state.homeState is HomeState.Content &&
                    state.homeState.repetitionsState is HomeFeature.RepetitionsState.Available
                ) {
                    state.copy(
                        homeState = state.homeState.copy(
                            repetitionsState = HomeFeature.RepetitionsState.Available(
                                max(state.homeState.repetitionsState.recommendedRepetitionsCount.dec(), 0)
                            )
                        )
                    ) to emptySet()
                } else {
                    null
                }
            // Click Messages
            is Message.ClickedContinueLearningOnWeb -> {
                if (state.homeState is HomeState.Content) {
                    state.copy(homeState = state.homeState.copy(isLoadingMagicLink = true)) to setOf(
                        Action.GetMagicLink(HyperskillUrlPath.Index()),
                        Action.LogAnalyticEvent(HomeClickedContinueLearningOnWebHyperskillAnalyticEvent())
                    )
                } else {
                    null
                }
            }
            is Message.ClickedTopicsRepetitionsCard ->
                if (state.homeState is HomeState.Content) {
                    state to setOf(
                        Action.ViewAction.NavigateTo.TopicsRepetitionsScreen,
                        Action.LogAnalyticEvent(
                            HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(
                                isCompleted = state.homeState.repetitionsState is HomeFeature.RepetitionsState.Available &&
                                    state.homeState.repetitionsState.recommendedRepetitionsCount == 0
                            )
                        )
                    )
                } else {
                    null
                }
            // MagicLinks Messages
            is Message.GetMagicLinkReceiveSuccess -> {
                if (state.homeState is HomeState.Content) {
                    state.copy(homeState = state.homeState.copy(isLoadingMagicLink = false)) to setOf(
                        Action.ViewAction.OpenUrl(message.url)
                    )
                } else {
                    null
                }
            }
            is Message.GetMagicLinkReceiveFailure -> {
                if (state.homeState is HomeState.Content) {
                    state.copy(homeState = state.homeState.copy(isLoadingMagicLink = false)) to setOf(
                        Action.ViewAction.ShowGetMagicLinkError
                    )
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
                if (state.homeState is HomeState.Content) {
                    when (state.homeState.problemOfDayState) {
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
            // Wrapper Messages
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
            is Message.TopicsToDiscoverNextMessage -> {
                val (topicsToDiscoverNextState, topicsToDiscoverNextActions) =
                    reduceTopicsToDiscoverNextMessage(state.topicsToDiscoverNextState, message.message)
                state.copy(topicsToDiscoverNextState = topicsToDiscoverNextState) to topicsToDiscoverNextActions
            }
        } ?: (state to emptySet())

    private fun reduceGamificationToolbarMessage(
        state: GamificationToolbarFeature.State,
        message: GamificationToolbarFeature.Message
    ): Pair<GamificationToolbarFeature.State, Set<Action>> {
        val (gamificationToolbarState, gamificationToolbarActions) = gamificationToolbarReducer.reduce(state, message)

        val actions = gamificationToolbarActions
            .map {
                if (it is GamificationToolbarFeature.Action.ViewAction) {
                    Action.ViewAction.GamificationToolbarViewAction(it)
                } else {
                    Action.GamificationToolbarAction(it)
                }
            }
            .toSet()

        return gamificationToolbarState to actions
    }

    private fun reduceTopicsToDiscoverNextMessage(
        state: TopicsToDiscoverNextFeature.State,
        message: TopicsToDiscoverNextFeature.Message
    ): Pair<TopicsToDiscoverNextFeature.State, Set<Action>> =
        // TODO: Uncomment after TopicsToDiscoverNextFeature will be implemented in Home
//        val (topicsToDiscoverNextState, topicsToDiscoverNextActions) =
//            topicsToDiscoverNextReducer.reduce(state, message)
//
//        val actions = topicsToDiscoverNextActions
//            .map {
//                if (it is TopicsToDiscoverNextFeature.Action.ViewAction) {
//                    Action.ViewAction.TopicsToDiscoverNextViewAction(it)
//                } else {
//                    Action.TopicsToDiscoverNextAction(it)
//                }
//            }
//            .toSet()
//
//        return topicsToDiscoverNextState to actions
        TopicsToDiscoverNextFeature.State.Idle to emptySet()
}
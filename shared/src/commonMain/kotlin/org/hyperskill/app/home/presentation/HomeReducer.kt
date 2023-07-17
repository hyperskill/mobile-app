package org.hyperskill.app.home.presentation

import kotlin.math.max
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.domain.analytic.HomeClickedContinueLearningOnWebHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeViewedHyperskillAnalyticEvent
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.HomeState
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetReducer
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

class HomeReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer,
    private val problemsLimitReducer: ProblemsLimitReducer,
    private val nextLearningActivityWidgetReducer: NextLearningActivityWidgetReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                initialize(state, message.forceUpdate)
            }
            is Message.HomeSuccess ->
                state.copy(
                    homeState = HomeState.Content(
                        message.problemOfDayState,
                        message.repetitionsState,
                        message.isFreemiumEnabled
                    )
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
                    GamificationToolbarFeature.Message.PullToRefresh
                )

                val (problemsLimitState, problemsLimitActions) = reduceProblemsLimitMessage(
                    state.problemsLimitState,
                    ProblemsLimitFeature.Message.PullToRefresh
                )

                val (nextLearningActivityWidgetState, nextLearningActivityWidgetActions) =
                    reduceNextLearningActivityWidgetMessage(
                        state.nextLearningActivityWidgetState,
                        NextLearningActivityWidgetFeature.InternalMessage.PullToRefresh
                    )

                state.copy(
                    homeState = homeState,
                    toolbarState = toolbarState,
                    problemsLimitState = problemsLimitState,
                    nextLearningActivityWidgetState = nextLearningActivityWidgetState
                ) to homeActions + toolbarActions + problemsLimitActions + nextLearningActivityWidgetActions
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
                    val isCompleted = state.homeState.repetitionsState is HomeFeature.RepetitionsState.Available &&
                        state.homeState.repetitionsState.recommendedRepetitionsCount == 0
                    state to setOf(
                        Action.ViewAction.NavigateTo.TopicsRepetitionsScreen,
                        Action.LogAnalyticEvent(
                            HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(isCompleted = isCompleted)
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedProblemOfDayCardReload -> {
                if (state.homeState is HomeState.Content) {
                    val (newState, newActions) = initialize(state, forceUpdate = true)
                    val analyticsEvent = when (state.homeState.problemOfDayState) {
                        HomeFeature.ProblemOfDayState.Empty -> null
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent(
                                isCompleted = false
                            )
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent(
                                isCompleted = true
                            )
                        }
                    }
                    val logEventAction = if (analyticsEvent != null) {
                        setOf(Action.LogAnalyticEvent(analyticsEvent))
                    } else {
                        emptySet()
                    }
                    newState to newActions + logEventAction
                } else {
                    null
                }
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
            is Message.ProblemsLimitMessage -> {
                val (problemsLimitState, problemsLimitActions) =
                    reduceProblemsLimitMessage(state.problemsLimitState, message.message)
                state.copy(problemsLimitState = problemsLimitState) to problemsLimitActions
            }
            is Message.NextLearningActivityWidgetMessage -> {
                val (nextLearningActivityWidgetState, nextLearningActivityWidgetActions) =
                    reduceNextLearningActivityWidgetMessage(state.nextLearningActivityWidgetState, message.message)
                state.copy(
                    nextLearningActivityWidgetState = nextLearningActivityWidgetState
                ) to nextLearningActivityWidgetActions
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

    private fun reduceProblemsLimitMessage(
        state: ProblemsLimitFeature.State,
        message: ProblemsLimitFeature.Message
    ): Pair<ProblemsLimitFeature.State, Set<Action>> {
        val (problemsLimitState, problemsLimitActions) =
            problemsLimitReducer.reduce(state, message)

        val actions = problemsLimitActions
            .map {
                if (it is ProblemsLimitFeature.Action.ViewAction) {
                    Action.ViewAction.ProblemsLimitViewAction(it)
                } else {
                    Action.ProblemsLimitAction(it)
                }
            }
            .toSet()

        return problemsLimitState to actions
    }

    private fun reduceNextLearningActivityWidgetMessage(
        state: NextLearningActivityWidgetFeature.State,
        message: NextLearningActivityWidgetFeature.Message
    ): Pair<NextLearningActivityWidgetFeature.State, Set<Action>> {
        val (nextLearningActivityWidgetState, nextLearningActivityWidgetActions) =
            nextLearningActivityWidgetReducer.reduce(state, message)

        val actions = nextLearningActivityWidgetActions
            .map {
                if (it is NextLearningActivityWidgetFeature.Action.ViewAction) {
                    Action.ViewAction.NextLearningActivityWidgetViewAction(it)
                } else {
                    Action.NextLearningActivityWidgetAction(it)
                }
            }
            .toSet()

        return nextLearningActivityWidgetState to actions
    }

    private fun initialize(state: State, forceUpdate: Boolean): Pair<State, Set<Action>> {
        val needReloadHome =
            state.homeState is HomeState.Idle ||
                forceUpdate && (state.homeState is HomeState.Content || state.homeState is HomeState.NetworkError)
        val (homeState, homeActions) =
            if (needReloadHome) {
                HomeState.Loading to setOf(Action.FetchHomeScreenData)
            } else {
                state.homeState to emptySet()
            }

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.Message.Initialize(forceUpdate)
            )

        val (problemsLimitState, problemsLimitActions) =
            reduceProblemsLimitMessage(
                state.problemsLimitState,
                ProblemsLimitFeature.Message.Initialize(forceUpdate)
            )

        val (nextLearningActivityWidgetState, nextLearningActivityWidgetActions) =
            reduceNextLearningActivityWidgetMessage(
                state.nextLearningActivityWidgetState,
                NextLearningActivityWidgetFeature.InternalMessage.Initialize(forceUpdate)
            )

        return state.copy(
            homeState = homeState,
            toolbarState = toolbarState,
            problemsLimitState = problemsLimitState,
            nextLearningActivityWidgetState = nextLearningActivityWidgetState
        ) to homeActions + toolbarActions + problemsLimitActions + nextLearningActivityWidgetActions
    }
}
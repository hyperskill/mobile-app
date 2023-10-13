package org.hyperskill.app.home.presentation

import kotlin.math.max
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeViewedHyperskillAnalyticEvent
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.HomeState
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias HomeReducerResult = Pair<State, Set<Action>>

class HomeReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): HomeReducerResult =
        when (message) {
            is Message.Initialize -> {
                initialize(state, message.forceUpdate)
            }
            is Message.HomeSuccess -> {
                state.copy(
                    homeState = HomeState.Content(
                        problemOfDayState = message.problemOfDayState,
                        repetitionsState = message.repetitionsState,
                        isFreemiumEnabled = message.isFreemiumEnabled
                    )
                ) to emptySet()
            }
            is Message.HomeFailure ->
                state.copy(homeState = HomeState.NetworkError) to emptySet()
            is Message.PullToRefresh -> {
                handlePullToRefresh(state)
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
            // Analytic Messages
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogAnalyticEvent(HomeViewedHyperskillAnalyticEvent()))
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
            is Message.StageImplementUnsupportedModalGoToHomeClicked ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.Home()
                        )
                    )
                )
            is Message.StageImplementUnsupportedModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.Home()
                        )
                    )
                )
            is Message.StageImplementUnsupportedModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.Home()
                        )
                    )
                )
            // Wrapper Messages
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
        } ?: (state to emptySet())

    private fun initialize(state: State, forceUpdate: Boolean): HomeReducerResult {
        val shouldReloadHome =
            state.homeState is HomeState.Idle ||
                forceUpdate && (state.homeState is HomeState.Content || state.homeState is HomeState.NetworkError)
        val (homeState, homeActions) =
            if (shouldReloadHome) {
                HomeState.Loading to setOf(Action.FetchHomeScreenData)
            } else {
                state.homeState to emptySet()
            }

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.Message.Initialize(forceUpdate)
            )

        return state.copy(
            homeState = homeState,
            toolbarState = toolbarState
        ) to homeActions + toolbarActions
    }

    private fun handlePullToRefresh(state: State): HomeReducerResult {
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

        return state.copy(
            homeState = homeState,
            toolbarState = toolbarState
        ) to homeActions + toolbarActions
    }

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
}
package org.hyperskill.app.home.presentation

import kotlin.math.max
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedProblemOfDayCardReloadHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent
import org.hyperskill.app.home.domain.analytic.HomeViewedHyperskillAnalyticEvent
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.HomeState
import org.hyperskill.app.home.presentation.HomeFeature.InternalAction
import org.hyperskill.app.home.presentation.HomeFeature.InternalMessage
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.home.presentation.HomeFeature.State
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias HomeReducerResult = Pair<State, Set<Action>>

internal class HomeReducer(
    private val gamificationToolbarReducer: GamificationToolbarReducer,
    private val challengeWidgetReducer: ChallengeWidgetReducer,
    private val interviewPreparationWidgetReducer: InterviewPreparationWidgetReducer
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
                        areProblemsLimited = message.areProblemsLimited
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
                    state to setOf(InternalAction.LaunchTimer)
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
            is InternalMessage.StepQuizSolved -> {
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
            is InternalMessage.TopicRepeated ->
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
            InternalMessage.TopicCompleted ->
                if (state.homeState is HomeState.Content &&
                    state.homeState.problemOfDayState is HomeFeature.ProblemOfDayState.Empty
                ) {
                    state to setOf(InternalAction.FetchProblemOfDayState)
                } else {
                    null
                }
            InternalMessage.FetchProblemOfDayStateResultError -> null
            is InternalMessage.FetchProblemOfDayStateResultSuccess ->
                if (state.homeState is HomeState.Content) {
                    state.copy(
                        homeState = state.homeState.copy(
                            problemOfDayState = message.problemOfDayState
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
                        InternalAction.LogAnalyticEvent(
                            HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(isCompleted = isCompleted)
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedProblemOfDayCardReload -> {
                if (state.homeState is HomeState.Content) {
                    val (newState, newActions) = initialize(state, forceUpdate = true)
                    val analyticEvent = when (state.homeState.problemOfDayState) {
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
                    val logEventAction = if (analyticEvent != null) {
                        setOf(InternalAction.LogAnalyticEvent(analyticEvent))
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
                state to setOf(InternalAction.LogAnalyticEvent(HomeViewedHyperskillAnalyticEvent()))
            is Message.ClickedProblemOfDayCardEventMessage -> {
                if (state.homeState is HomeState.Content) {
                    when (state.homeState.problemOfDayState) {
                        is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                            state to setOf(
                                InternalAction.LogAnalyticEvent(
                                    HomeClickedProblemOfDayCardHyperskillAnalyticEvent(
                                        isCompleted = false
                                    )
                                )
                            )
                        }
                        is HomeFeature.ProblemOfDayState.Solved -> {
                            state to setOf(
                                InternalAction.LogAnalyticEvent(
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
            is Message.ChallengeWidgetMessage -> {
                val (challengeWidgetState, challengeWidgetActions) =
                    reduceChallengeWidgetMessage(state.challengeWidgetState, message.message)
                state.copy(challengeWidgetState = challengeWidgetState) to challengeWidgetActions
            }
            is Message.InterviewPreparationWidgetMessage -> {
                val (interviewPreparationWidgetState, interviewPreparationWidgetActions) =
                    reduceInterviewPreparationWidgetMessage(state.interviewPreparationWidgetState, message.message)
                state.copy(
                    interviewPreparationWidgetState = interviewPreparationWidgetState
                ) to interviewPreparationWidgetActions
            }
        } ?: (state to emptySet())

    private fun initialize(state: State, forceUpdate: Boolean): HomeReducerResult {
        val shouldReloadHome =
            state.homeState is HomeState.Idle ||
                forceUpdate && (state.homeState is HomeState.Content || state.homeState is HomeState.NetworkError)
        val (homeState, homeActions) =
            if (shouldReloadHome) {
                HomeState.Loading to setOf(InternalAction.FetchHomeScreenData)
            } else {
                state.homeState to emptySet()
            }

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.Initialize(forceUpdate)
            )

        val (challengeWidgetState, challengeWidgetActions) =
            reduceChallengeWidgetMessage(
                state.challengeWidgetState,
                ChallengeWidgetFeature.InternalMessage.Initialize(forceUpdate)
            )

        val (interviewPreparationWidgetState, interviewPreparationWidgetActions) =
            reduceInterviewPreparationWidgetMessage(
                state.interviewPreparationWidgetState,
                InterviewPreparationWidgetFeature.InternalMessage.Initialize(forceUpdate)
            )

        return state.copy(
            homeState = homeState,
            toolbarState = toolbarState,
            challengeWidgetState = challengeWidgetState,
            interviewPreparationWidgetState = interviewPreparationWidgetState
        ) to homeActions + toolbarActions + challengeWidgetActions + interviewPreparationWidgetActions
    }

    private fun handlePullToRefresh(state: State): HomeReducerResult {
        val (homeState, homeActions) = if (
            state.homeState is HomeState.Content && !state.homeState.isRefreshing
        ) {
            state.homeState.copy(isRefreshing = true) to setOf(
                InternalAction.FetchHomeScreenData,
                InternalAction.LogAnalyticEvent(HomeClickedPullToRefreshHyperskillAnalyticEvent())
            )
        } else {
            state.homeState to emptySet()
        }

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.PullToRefresh
            )

        val (challengeWidgetState, challengeWidgetActions) =
            reduceChallengeWidgetMessage(
                state.challengeWidgetState,
                ChallengeWidgetFeature.InternalMessage.PullToRefresh
            )

        val (interviewPreparationWidgetState, interviewPreparationWidgetAction) =
            reduceInterviewPreparationWidgetMessage(
                state.interviewPreparationWidgetState,
                InterviewPreparationWidgetFeature.InternalMessage.PullToRefresh
            )

        return state.copy(
            homeState = homeState,
            toolbarState = toolbarState,
            challengeWidgetState = challengeWidgetState,
            interviewPreparationWidgetState = interviewPreparationWidgetState
        ) to homeActions + toolbarActions + challengeWidgetActions + interviewPreparationWidgetAction
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
                    InternalAction.GamificationToolbarAction(it)
                }
            }
            .toSet()

        return gamificationToolbarState to actions
    }

    private fun reduceChallengeWidgetMessage(
        state: ChallengeWidgetFeature.State,
        message: ChallengeWidgetFeature.Message
    ): Pair<ChallengeWidgetFeature.State, Set<Action>> {
        val (challengeWidgetState, challengeWidgetActions) = challengeWidgetReducer.reduce(state, message)

        val actions = challengeWidgetActions
            .map {
                if (it is ChallengeWidgetFeature.Action.ViewAction) {
                    Action.ViewAction.ChallengeWidgetViewAction(it)
                } else {
                    InternalAction.ChallengeWidgetAction(it)
                }
            }
            .toSet()

        return challengeWidgetState to actions
    }

    private fun reduceInterviewPreparationWidgetMessage(
        state: InterviewPreparationWidgetFeature.State,
        message: InterviewPreparationWidgetFeature.Message
    ): Pair<InterviewPreparationWidgetFeature.State, Set<Action>> {
        val (interviewPreparationWidgetState, interviewPreparationWidgetActions) =
            interviewPreparationWidgetReducer.reduce(state, message)

        val actions = interviewPreparationWidgetActions
            .map {
                if (it is InterviewPreparationWidgetFeature.Action.ViewAction) {
                    Action.ViewAction.InterviewPreparationWidgetViewAction(it)
                } else {
                    InternalAction.InterviewPreparationWidgetAction(it)
                }
            }
            .toSet()

        return interviewPreparationWidgetState to actions
    }
}
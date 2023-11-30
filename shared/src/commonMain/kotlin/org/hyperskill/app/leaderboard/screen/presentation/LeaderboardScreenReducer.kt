package org.hyperskill.app.leaderboard.screen.presentation

import org.hyperskill.app.core.utils.DateTimeUtils
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboard.screen.domain.analytic.LeaderboardClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.leaderboard.screen.domain.analytic.LeaderboardClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.leaderboard.screen.domain.analytic.LeaderboardClickedTabHyperskillAnalyticEvent
import org.hyperskill.app.leaderboard.screen.domain.analytic.LeaderboardViewedHyperskillAnalyticEvent
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.State
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias LeaderboardScreenReducerResult = Pair<State, Set<Action>>

internal class LeaderboardScreenReducer(
    private val leaderWidgetReducer: LeaderboardWidgetReducer,
    private val gamificationToolbarReducer: GamificationToolbarReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): LeaderboardScreenReducerResult =
        when (message) {
            Message.Initialize -> {
                initializeFeatures(state, retryContentLoadingClicked = false)
            }
            Message.RetryContentLoading -> {
                initializeFeatures(state, retryContentLoadingClicked = true)
            }
            Message.PullToRefresh -> {
                handlePullToRefreshMessage(state)
            }
            Message.ScreenBecomesActive -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(
                        state.leaderboardWidgetState,
                        LeaderboardWidgetFeature.InternalMessage.ReloadContentInBackground
                    )
                state.copy(leaderboardWidgetState = leaderboardWidgetState) to leaderboardWidgetActions
            }
            is Message.TabClicked -> {
                if (state.currentTab != message.tab) {
                    state.copy(currentTab = message.tab) to setOf(
                        InternalAction.LogAnalyticEvent(LeaderboardClickedTabHyperskillAnalyticEvent(message.tab))
                    )
                } else {
                    null
                }
            }
            is Message.ListItemClicked -> {
                handleListItemClickedMessage(state, message)
            }
            // Timer Messages
            LeaderboardScreenFeature.InternalMessage.DailyLeaderboardTimerCompleted -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(
                        state.leaderboardWidgetState,
                        LeaderboardWidgetFeature.InternalMessage.Initialize(forceUpdate = true)
                    )

                val secondsUntilStartOfNextDayInNewYork = DateTimeUtils.secondsUntilStartOfNextDayInNewYork()

                state.copy(
                    leaderboardWidgetState = leaderboardWidgetState,
                    dailyLeaderboardSecondsUntilNextUpdate = secondsUntilStartOfNextDayInNewYork
                ) to leaderboardWidgetActions + setOf(
                    InternalAction.LaunchDailyLeaderboardTimer(secondsUntilStartOfNextDayInNewYork)
                )
            }
            is LeaderboardScreenFeature.InternalMessage.DailyLeaderboardTimerTick -> {
                state.copy(dailyLeaderboardSecondsUntilNextUpdate = message.secondsUntilNextUpdate) to emptySet()
            }
            LeaderboardScreenFeature.InternalMessage.WeeklyLeaderboardTimerCompleted -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(
                        state.leaderboardWidgetState,
                        LeaderboardWidgetFeature.InternalMessage.Initialize(forceUpdate = true)
                    )

                val secondsUntilNextSundayInNewYork = DateTimeUtils.secondsUntilNextSundayInNewYork()

                state.copy(
                    leaderboardWidgetState = leaderboardWidgetState,
                    weeklyLeaderboardSecondsUntilNextUpdate = secondsUntilNextSundayInNewYork
                ) to leaderboardWidgetActions + setOf(
                    InternalAction.LaunchWeeklyLeaderboardTimer(secondsUntilNextSundayInNewYork)
                )
            }
            is LeaderboardScreenFeature.InternalMessage.WeeklyLeaderboardTimerTick -> {
                state.copy(weeklyLeaderboardSecondsUntilNextUpdate = message.secondsUntilNextUpdate) to emptySet()
            }
            // Analytic Messages
            Message.ViewedEventMessage -> {
                state to setOf(InternalAction.LogAnalyticEvent(LeaderboardViewedHyperskillAnalyticEvent))
            }
            // Wrapper Messages
            is Message.LeaderboardWidgetMessage -> {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(state.leaderboardWidgetState, message.message)
                state.copy(leaderboardWidgetState = leaderboardWidgetState) to leaderboardWidgetActions
            }
            is Message.GamificationToolbarMessage -> {
                val (toolbarState, toolbarActions) =
                    reduceGamificationToolbarMessage(state.toolbarState, message.message)
                state.copy(toolbarState = toolbarState) to toolbarActions
            }
        } ?: (state to emptySet())

    private fun initializeFeatures(
        state: State,
        retryContentLoadingClicked: Boolean
    ): LeaderboardScreenReducerResult {
        val (screenState, screenActions) = if (state.leaderboardWidgetState is LeaderboardWidgetFeature.State.Idle) {
            val secondsUntilStartOfNextDayInNewYork = DateTimeUtils.secondsUntilStartOfNextDayInNewYork()
            val secondsUntilNextSundayInNewYork = DateTimeUtils.secondsUntilNextSundayInNewYork()
            state.copy(
                dailyLeaderboardSecondsUntilNextUpdate = secondsUntilStartOfNextDayInNewYork,
                weeklyLeaderboardSecondsUntilNextUpdate = secondsUntilNextSundayInNewYork
            ) to setOf(
                InternalAction.LaunchDailyLeaderboardTimer(secondsUntilStartOfNextDayInNewYork),
                InternalAction.LaunchWeeklyLeaderboardTimer(secondsUntilNextSundayInNewYork)
            )
        } else {
            state to emptySet()
        }

        val (leaderboardWidgetState, leaderboardWidgetActions) =
            reduceLeaderboardWidgetMessage(
                screenState.leaderboardWidgetState,
                LeaderboardWidgetFeature.InternalMessage.Initialize(forceUpdate = retryContentLoadingClicked)
            )

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                screenState.toolbarState,
                GamificationToolbarFeature.InternalMessage.Initialize(retryContentLoadingClicked)
            )

        val analyticActions = if (retryContentLoadingClicked) {
            setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedRetryContentLoadingHyperskillAnalyticEvent))
        } else {
            emptySet()
        }

        return screenState.copy(
            leaderboardWidgetState = leaderboardWidgetState,
            toolbarState = toolbarState
        ) to (screenActions + leaderboardWidgetActions + toolbarActions + analyticActions)
    }

    private fun handlePullToRefreshMessage(state: State): LeaderboardScreenReducerResult {
        val (leaderboardWidgetState, leaderboardWidgetActions) =
            reduceLeaderboardWidgetMessage(
                state.leaderboardWidgetState,
                LeaderboardWidgetFeature.InternalMessage.PullToRefresh
            )

        val (toolbarState, toolbarActions) =
            reduceGamificationToolbarMessage(
                state.toolbarState,
                GamificationToolbarFeature.InternalMessage.PullToRefresh
            )

        val analyticActions =
            setOf(InternalAction.LogAnalyticEvent(LeaderboardClickedPullToRefreshHyperskillAnalyticEvent))

        return state.copy(
            leaderboardWidgetState = leaderboardWidgetState,
            toolbarState = toolbarState
        ) to (leaderboardWidgetActions + toolbarActions + analyticActions)
    }

    private fun handleListItemClickedMessage(
        state: State,
        message: Message.ListItemClicked
    ): LeaderboardScreenReducerResult? =
        if (message.listItem is LeaderboardWidgetListItem.UserInfo &&
            state.leaderboardWidgetState is LeaderboardWidgetFeature.State.Content
        ) {
            val targetLeaderboardItem = when (state.currentTab) {
                LeaderboardScreenFeature.Tab.DAY -> state.leaderboardWidgetState.dailyLeaderboard
                LeaderboardScreenFeature.Tab.WEEK -> state.leaderboardWidgetState.weeklyLeaderboard
            }.firstOrNull { it.user.id == message.listItem.userId }

            if (targetLeaderboardItem != null) {
                val (leaderboardWidgetState, leaderboardWidgetActions) =
                    reduceLeaderboardWidgetMessage(
                        state.leaderboardWidgetState,
                        LeaderboardWidgetFeature.InternalMessage.LeaderboardItemClickedEventMessage(
                            currentTab = state.currentTab,
                            leaderboardItem = targetLeaderboardItem
                        )
                    )
                state.copy(leaderboardWidgetState = leaderboardWidgetState) to leaderboardWidgetActions
            } else {
                null
            }
        } else {
            null
        }

    private fun reduceLeaderboardWidgetMessage(
        state: LeaderboardWidgetFeature.State,
        message: LeaderboardWidgetFeature.Message
    ): Pair<LeaderboardWidgetFeature.State, Set<Action>> {
        val (leaderboardWidgetState, leaderboardWidgetActions) = leaderWidgetReducer.reduce(state, message)

        val actions = leaderboardWidgetActions
            .map {
                if (it is LeaderboardWidgetFeature.Action.ViewAction) {
                    Action.ViewAction.LeaderboardWidgetViewAction(it)
                } else {
                    InternalAction.LeaderboardWidgetAction(it)
                }
            }
            .toSet()

        return leaderboardWidgetState to actions
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
}
package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedProblemsLimitHSAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedProgressHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedSearchHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedStreakHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalAction
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalMessage
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.State
import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.StreakState
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias GamificationToolbarReducerResult = Pair<State, Set<Action>>

class GamificationToolbarReducer(
    private val screen: GamificationToolbarScreen
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): GamificationToolbarReducerResult =
        when (message) {
            is InternalMessage.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(InternalAction.FetchGamificationToolbarData(screen, message.forceUpdate))
                } else {
                    null
                }
            is InternalMessage.FetchGamificationToolbarDataError ->
                State.Error to emptySet()
            is InternalMessage.FetchGamificationToolbarDataSuccess ->
                createContentState(
                    message.gamificationToolbarData,
                    message.subscription,
                    message.chargeLimitsStrategy
                ) to emptySet()
            is InternalMessage.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) {
                            null
                        } else {
                            state.copy(isRefreshing = true) to
                                setOf(InternalAction.FetchGamificationToolbarData(screen, true))
                        }
                    is State.Error ->
                        State.Loading to setOf(InternalAction.FetchGamificationToolbarData(screen, true))
                    else ->
                        null
                }
            // Flow Messages
            is InternalMessage.StepSolved ->
                if (state is State.Content && state.historicalStreak.state == StreakState.NOTHING) {
                    state.copy(
                        historicalStreak = HistoricalStreak(StreakState.COMPLETED),
                        currentStreak = state.currentStreak + 1
                    ) to emptySet()
                } else {
                    null
                }
            is InternalMessage.StreakChanged ->
                if (state is State.Content && message.streak != null) {
                    state.copy(
                        currentStreak = message.streak.currentStreak,
                        historicalStreak = message.streak.history.firstOrNull() ?: state.historicalStreak
                    ) to emptySet()
                } else {
                    null
                }
            is InternalMessage.StudyPlanChanged -> {
                if (state is State.Content) {
                    if (message.studyPlan.trackId != null) {
                        state to setOf(
                            InternalAction.FetchGamificationToolbarData(screen, forceUpdate = true)
                        )
                    } else {
                        state.copy(trackProgress = null) to emptySet()
                    }
                } else {
                    null
                }
            }
            is InternalMessage.TopicCompleted -> {
                if (state is State.Content) {
                    state to setOf(
                        InternalAction.FetchGamificationToolbarData(screen, forceUpdate = true)
                    )
                } else {
                    null
                }
            }
            is InternalMessage.GamificationToolbarDataChanged -> {
                when (state) {
                    is State.Content -> {
                        if (state.isRefreshing) {
                            null
                        } else {
                            state.copy(
                                trackProgress = message.gamificationToolbarData.trackProgress,
                                currentStreak = message.gamificationToolbarData.currentStreak,
                                historicalStreak = HistoricalStreak(message.gamificationToolbarData.streakState)
                            )to emptySet()
                        }
                    }
                    else -> null
                }
            }
            is InternalMessage.SubscriptionChanged -> handleSubscriptionChanged(state, message)
            // Click Messages
            is Message.ClickedStreak ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        InternalAction.LogAnalyticEvent(
                            GamificationToolbarClickedStreakHyperskillAnalyticEvent(screen)
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedProgress ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProgressScreen,
                        InternalAction.LogAnalyticEvent(
                            GamificationToolbarClickedProgressHyperskillAnalyticEvent(screen)
                        )
                    )
                } else {
                    null
                }
            is Message.ClickedSearch ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowSearchScreen,
                        InternalAction.LogAnalyticEvent(
                            GamificationToolbarClickedSearchHyperskillAnalyticEvent(screen)
                        )
                    )
                } else {
                    null
                }
            Message.ProblemsLimitClicked -> handleProblemsLimitClicked(state)
        } ?: (state to emptySet())

    private fun handleSubscriptionChanged(
        state: State,
        message: InternalMessage.SubscriptionChanged
    ): GamificationToolbarReducerResult =
        when (state) {
            is State.Content ->
                if (state.isRefreshing) {
                    state to emptySet()
                } else {
                    state.copy(subscription = message.subscription) to emptySet()
                }
            else -> state to emptySet()
        }

    private fun handleProblemsLimitClicked(state: State): GamificationToolbarReducerResult =
        if (state is State.Content) {
            state to setOf(
                InternalAction.LogAnalyticEvent(
                    GamificationToolbarClickedProblemsLimitHSAnalyticEvent(screen)
                )
            )
        } else {
            state to emptySet()
        }

    private fun createContentState(
        gamificationToolbarData: GamificationToolbarData,
        subscription: Subscription,
        chargeLimitsStrategy: FreemiumChargeLimitsStrategy
    ): State.Content =
        State.Content(
            trackProgress = gamificationToolbarData.trackProgress,
            currentStreak = gamificationToolbarData.currentStreak,
            historicalStreak = HistoricalStreak(gamificationToolbarData.streakState),
            subscription = subscription,
            chargeLimitsStrategy = chargeLimitsStrategy
        )
}
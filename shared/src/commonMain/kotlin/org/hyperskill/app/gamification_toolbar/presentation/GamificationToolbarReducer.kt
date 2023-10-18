package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedGemsHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedProgressHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedStreakHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.State
import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.StreakState
import ru.nobird.app.presentation.redux.reducer.StateReducer

class GamificationToolbarReducer(
    private val screen: GamificationToolbarScreen
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchGamificationToolbarData(screen, message.forceUpdate))
                } else {
                    null
                }
            is Message.FetchGamificationToolbarDataError ->
                State.Error to emptySet()
            is Message.FetchGamificationToolbarDataSuccess ->
                State.Content(
                    trackProgress = message.gamificationToolbarData.trackProgress,
                    currentStreak = message.gamificationToolbarData.currentStreak,
                    historicalStreak = HistoricalStreak(message.gamificationToolbarData.streakState),
                    hypercoinsBalance = message.gamificationToolbarData.hypercoinsBalance
                ) to emptySet()
            is Message.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) {
                            null
                        } else {
                            state.copy(isRefreshing = true) to
                                setOf(Action.FetchGamificationToolbarData(screen, true))
                        }
                    is State.Error ->
                        State.Loading to setOf(Action.FetchGamificationToolbarData(screen, true))
                    else ->
                        null
                }
            // Flow Messages
            is Message.StepSolved ->
                if (state is State.Content && state.historicalStreak.state == StreakState.NOTHING) {
                    state.copy(
                        historicalStreak = HistoricalStreak(StreakState.COMPLETED),
                        currentStreak = state.currentStreak + 1
                    ) to emptySet()
                } else {
                    null
                }
            is Message.HypercoinsBalanceChanged ->
                if (state is State.Content) {
                    state.copy(
                        hypercoinsBalance = message.hypercoinsBalance
                    ) to emptySet()
                } else {
                    null
                }
            is Message.StreakChanged ->
                if (state is State.Content && message.streak != null) {
                    state.copy(
                        currentStreak = message.streak.currentStreak,
                        historicalStreak = message.streak.history.firstOrNull() ?: state.historicalStreak
                    ) to emptySet()
                } else {
                    null
                }
            is Message.StudyPlanChanged -> {
                if (state is State.Content) {
                    if (message.studyPlan.trackId != null) {
                        state to setOf(
                            Action.FetchGamificationToolbarData(screen, forceUpdate = true)
                        )
                    } else {
                        state.copy(trackProgress = null) to emptySet()
                    }
                } else {
                    null
                }
            }
            is Message.TopicCompleted -> {
                if (state is State.Content) {
                    state to setOf(
                        Action.FetchGamificationToolbarData(screen, forceUpdate = true)
                    )
                } else {
                    null
                }
            }
            // Click Messages
            is Message.ClickedGems ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        Action.LogAnalyticEvent(GamificationToolbarClickedGemsHyperskillAnalyticEvent(screen))
                    )
                } else {
                    null
                }
            is Message.ClickedStreak ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        Action.LogAnalyticEvent(GamificationToolbarClickedStreakHyperskillAnalyticEvent(screen))
                    )
                } else {
                    null
                }
            is Message.ClickedProgress ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProgressScreen,
                        Action.LogAnalyticEvent(GamificationToolbarClickedProgressHyperskillAnalyticEvent(screen))
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
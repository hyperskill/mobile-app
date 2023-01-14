package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedGemsHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.analytic.GamificationToolbarClickedStreakHyperskillAnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class GamificationToolbarReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize ->
                if (state is State.Idle ||
                    (message.forceUpdate && (state is State.Content || state is State.Error))
                ) {
                    State.Loading to setOf(Action.FetchNavigationBarItems(message.screen))
                } else {
                    null
                }
            is Message.NavigationBarItemsError ->
                State.Error to emptySet()
            is Message.NavigationBarItemsSuccess ->
                State.Content(message.streak, message.hypercoinsBalance) to emptySet()
            is Message.PullToRefresh ->
                when (state) {
                    is State.Content ->
                        if (state.isRefreshing) null
                        else state.copy(isRefreshing = true) to setOf(Action.FetchNavigationBarItems(message.screen))
                    is State.Error ->
                        State.Loading to setOf(Action.FetchNavigationBarItems(message.screen))
                    else ->
                        null
                }
            // Flow Messages
            is Message.StepSolved ->
                if (state is State.Content) {
                    state.copy(streak = state.streak?.getStreakWithTodaySolved()) to emptySet()
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
            is Message.ClickedGems ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        Action.LogAnalyticEvent(GamificationToolbarClickedGemsHyperskillAnalyticEvent(message.screen))
                    )
                } else {
                    null
                }
            is Message.ClickedStreak ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        Action.LogAnalyticEvent(GamificationToolbarClickedStreakHyperskillAnalyticEvent(message.screen))
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
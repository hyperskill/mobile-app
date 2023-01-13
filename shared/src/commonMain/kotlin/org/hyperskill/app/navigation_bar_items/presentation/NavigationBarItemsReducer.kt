package org.hyperskill.app.navigation_bar_items.presentation

import org.hyperskill.app.navigation_bar_items.domain.analytic.NavigationBarItemsClickedGemsHyperskillAnalyticEvent
import org.hyperskill.app.navigation_bar_items.domain.analytic.NavigationBarItemsClickedStreakHyperskillAnalyticEvent
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature.Action
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature.Message
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class NavigationBarItemsReducer : StateReducer<State, Message, Action> {
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
                        Action.LogAnalyticEvent(NavigationBarItemsClickedGemsHyperskillAnalyticEvent(message.screen))
                    )
                } else {
                    null
                }
            is Message.ClickedStreak ->
                if (state is State.Content) {
                    state to setOf(
                        Action.ViewAction.ShowProfileTab,
                        Action.LogAnalyticEvent(NavigationBarItemsClickedStreakHyperskillAnalyticEvent(message.screen))
                    )
                } else {
                    null
                }
        } ?: (state to emptySet())
}
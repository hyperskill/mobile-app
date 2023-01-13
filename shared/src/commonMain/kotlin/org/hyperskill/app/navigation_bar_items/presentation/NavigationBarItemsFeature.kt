package org.hyperskill.app.navigation_bar_items.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.navigation_bar_items.domain.model.NavigationBarItemsScreen
import org.hyperskill.app.streaks.domain.model.Streak

interface NavigationBarItemsFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val streak: Streak?,
            val hypercoinsBalance: Int
        ) : State
    }

    sealed interface Message {
        /**
         * Initialization
         */
        data class Initialize(val screen: NavigationBarItemsScreen, val forceUpdate: Boolean = false) : Message
        object NavigationBarItemsError : Message
        data class NavigationBarItemsSuccess(
            val streak: Streak?,
            val hypercoinsBalance: Int
        ) : Message

        /**
         * Flow Messages
         */
        object StepSolved : Message
        data class HypercoinsBalanceChanged(val hypercoinsBalance: Int) : Message

        /**
         * Clicks
         */
        data class ClickedGems(val screen: NavigationBarItemsScreen) : Message
        data class ClickedStreak(val screen: NavigationBarItemsScreen) : Message
    }

    sealed interface Action {
        data class FetchNavigationBarItems(val screen: NavigationBarItemsScreen) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowProfileTab : ViewAction
        }
    }
}
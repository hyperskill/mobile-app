package org.hyperskill.app.navigation_bar_items.presentation

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
        data class Initialize(val forceUpdate: Boolean = false) : Message
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
    }

    sealed interface Action {
        object FetchNavigationBarItems : Action
    }
}
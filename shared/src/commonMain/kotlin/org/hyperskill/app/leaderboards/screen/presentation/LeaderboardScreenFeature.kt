package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing

object LeaderboardScreenFeature {
    internal data class State(
        val leaderboardState: LeaderboardState,
        val toolbarState: GamificationToolbarFeature.State
    ) {
        val isRefreshing: Boolean
            get() = leaderboardState is LeaderboardState.Content && leaderboardState.isRefreshing ||
                toolbarState.isRefreshing
    }

    internal sealed interface LeaderboardState {
        object Idle : LeaderboardState
        object Loading : LeaderboardState
        object Error : LeaderboardState
        data class Content(
            val isRefreshing: Boolean = false
        ) : LeaderboardState
    }

    data class ViewState(
        val leaderboardViewState: LeaderboardViewState,
        val toolbarState: GamificationToolbarFeature.State,
        val isRefreshing: Boolean
    )

    sealed interface LeaderboardViewState {
        object Idle : LeaderboardViewState
        object Loading : LeaderboardViewState
        object Error : LeaderboardViewState
        object Empty : LeaderboardViewState
        object Content : LeaderboardViewState
    }

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object PullToRefresh : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message

        /**
         * Message Wrappers
         */
        data class GamificationToolbarMessage(
            val message: GamificationToolbarFeature.Message
        ) : Message
    }

    internal sealed interface InternalMessage : Message {
        object FetchLeaderboardsError : InternalMessage
        object FetchLeaderboardsSuccess : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            /**
             * ViewAction Wrappers
             */
            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchLeaderboards : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        /**
         * Action Wrappers
         */
        data class GamificationToolbarAction(
            val action: GamificationToolbarFeature.Action
        ) : InternalAction
    }
}
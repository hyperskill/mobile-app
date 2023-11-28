package org.hyperskill.app.leaderboards.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature.isRefreshing
import org.hyperskill.app.leaderboards.widget.view.model.LeaderboardWidgetListItem

object LeaderboardScreenFeature {
    internal data class State(
        val currentTab: Tab,
        val leaderboardWidgetState: LeaderboardWidgetFeature.State,
        val toolbarState: GamificationToolbarFeature.State
    ) {
        val isRefreshing: Boolean
            get() = leaderboardWidgetState.isRefreshing || toolbarState.isRefreshing
    }

    data class ViewState(
        val currentTab: Tab,
        val listViewState: ListViewState,
        val toolbarState: GamificationToolbarFeature.State,
        val isRefreshing: Boolean
    )

    sealed interface ListViewState {
        object Idle : ListViewState
        object Loading : ListViewState
        object Error : ListViewState
        data class Content(
            val list: List<LeaderboardWidgetListItem>
        ) : ListViewState
    }

    enum class Tab {
        DAY,
        WEEK
    }

    internal fun initialState(initialTab: Tab = Tab.DAY): State =
        State(
            currentTab = initialTab,
            leaderboardWidgetState = LeaderboardWidgetFeature.State.Idle,
            toolbarState = GamificationToolbarFeature.State.Idle,
        )

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object PullToRefresh : Message

        object ScreenBecomesActive : Message

        data class TabClicked(val tab: Tab) : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message

        /**
         * Message Wrappers
         */
        data class LeaderboardWidgetMessage(
            val message: LeaderboardWidgetFeature.Message
        ) : Message

        data class GamificationToolbarMessage(
            val message: GamificationToolbarFeature.Message
        ) : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            /**
             * ViewAction Wrappers
             */
            data class LeaderboardWidgetViewAction(
                val viewAction: LeaderboardWidgetFeature.Action.ViewAction
            ) : ViewAction

            data class GamificationToolbarViewAction(
                val viewAction: GamificationToolbarFeature.Action.ViewAction
            ) : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction

        /**
         * Action Wrappers
         */
        data class LeaderboardWidgetAction(
            val action: LeaderboardWidgetFeature.Action
        ) : InternalAction

        data class GamificationToolbarAction(
            val action: GamificationToolbarFeature.Action
        ) : InternalAction
    }
}
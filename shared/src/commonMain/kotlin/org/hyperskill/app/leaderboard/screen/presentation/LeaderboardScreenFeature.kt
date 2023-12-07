package org.hyperskill.app.leaderboard.screen.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.isRefreshing
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature
import org.hyperskill.app.leaderboard.widget.presentation.LeaderboardWidgetFeature.isRefreshing
import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem

object LeaderboardScreenFeature {
    internal data class State(
        val currentTab: Tab,
        val leaderboardWidgetState: LeaderboardWidgetFeature.State,
        val toolbarState: GamificationToolbarFeature.State,
        val dailyLeaderboardSecondsUntilNextUpdate: Long?,
        val weeklyLeaderboardSecondsUntilNextUpdate: Long?
    ) {
        val isRefreshing: Boolean
            get() = leaderboardWidgetState.isRefreshing || toolbarState.isRefreshing
    }

    data class ViewState(
        val currentTab: Tab,
        val listViewState: ListViewState,
        val toolbarViewState: GamificationToolbarFeature.ViewState,
        val isRefreshing: Boolean,
        val updatesInText: String?
    )

    sealed interface ListViewState {
        object Idle : ListViewState
        object Loading : ListViewState
        object Error : ListViewState
        object Empty : ListViewState
        data class Content(
            val list: List<LeaderboardWidgetListItem>
        ) : ListViewState
    }

    enum class Tab() {
        DAY,
        WEEK
    }

    internal fun initialState(initialTab: Tab = Tab.DAY): State =
        State(
            currentTab = initialTab,
            leaderboardWidgetState = LeaderboardWidgetFeature.State.Idle,
            toolbarState = GamificationToolbarFeature.State.Idle,
            dailyLeaderboardSecondsUntilNextUpdate = null,
            weeklyLeaderboardSecondsUntilNextUpdate = null
        )

    sealed interface Message {
        object Initialize : Message
        object RetryContentLoading : Message

        object PullToRefresh : Message

        object ScreenBecomesActive : Message

        data class TabClicked(val tab: Tab) : Message
        data class ListItemClicked(val userId: Long) : Message

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

    internal sealed interface InternalMessage : Message {
        object DailyLeaderboardTimerCompleted : InternalMessage
        data class DailyLeaderboardTimerTick(val secondsUntilNextUpdate: Long) : InternalMessage

        object WeeklyLeaderboardTimerCompleted : InternalMessage
        data class WeeklyLeaderboardTimerTick(val secondsUntilNextUpdate: Long) : InternalMessage
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

        data class LaunchDailyLeaderboardTimer(val secondsUntilStartOfNextDay: Long) : InternalAction
        data class LaunchWeeklyLeaderboardTimer(val secondsUntilNextSunday: Long) : InternalAction

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
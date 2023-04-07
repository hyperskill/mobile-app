package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.track.domain.model.TrackWithProgress

interface GamificationToolbarFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val streak: Streak?,
            val hypercoinsBalance: Int,
            val trackWithProgress: TrackWithProgress?,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    sealed interface Message {
        /**
         * Initialization
         */
        data class Initialize(val screen: GamificationToolbarScreen, val forceUpdate: Boolean = false) : Message
        object FetchGamificationToolbarDataError : Message
        data class FetchGamificationToolbarDataSuccess(
            val streak: Streak?,
            val hypercoinsBalance: Int,
            val trackWithProgress: TrackWithProgress?
        ) : Message

        data class PullToRefresh(val screen: GamificationToolbarScreen) : Message

        /**
         * Flow Messages
         */
        object StepSolved : Message
        data class HypercoinsBalanceChanged(val hypercoinsBalance: Int) : Message
        data class StreakChanged(val streak: Streak?) : Message

        /**
         * Clicks
         */
        data class ClickedGems(val screen: GamificationToolbarScreen) : Message
        data class ClickedStreak(val screen: GamificationToolbarScreen) : Message
    }

    sealed interface Action {
        data class FetchGamificationToolbarData(
            val screen: GamificationToolbarScreen,
            val forceUpdate: Boolean
        ) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowProfileTab : ViewAction
        }
    }
}
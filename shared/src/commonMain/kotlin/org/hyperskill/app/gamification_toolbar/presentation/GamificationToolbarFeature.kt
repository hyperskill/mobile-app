package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.TrackWithProgress

object GamificationToolbarFeature {
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

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface Message {
        /**
         * Initialization
         */
        data class Initialize(val forceUpdate: Boolean = false) : Message

        object FetchGamificationToolbarDataError : Message
        data class FetchGamificationToolbarDataSuccess(
            val streak: Streak?,
            val hypercoinsBalance: Int,
            val trackWithProgress: TrackWithProgress?
        ) : Message

        sealed interface FetchTrackWithProgressResult : Message {
            data class Success(val trackWithProgress: TrackWithProgress?) : FetchTrackWithProgressResult
            object Error : FetchTrackWithProgressResult
        }

        object PullToRefresh : Message

        /**
         * Flow Messages
         */
        object StepSolved : Message
        data class HypercoinsBalanceChanged(val hypercoinsBalance: Int) : Message
        data class StreakChanged(val streak: Streak?) : Message
        data class StudyPlanChanged(val studyPlan: StudyPlan) : Message
        object TopicCompleted : Message

        /**
         * Clicks
         */
        object ClickedGems : Message
        object ClickedStreak : Message
        object ClickedProgress : Message
    }

    sealed interface Action {
        data class FetchGamificationToolbarData(
            val screen: GamificationToolbarScreen,
            val forceUpdate: Boolean
        ) : Action

        data class FetchTrackWithProgress(
            val trackId: Long,
            val transaction: HyperskillSentryTransaction
        ) : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        sealed interface ViewAction : Action {
            object ShowProfileTab : ViewAction
            sealed interface NavigateTo : ViewAction {
                object ProgressScreen : NavigateTo
            }
        }
    }
}
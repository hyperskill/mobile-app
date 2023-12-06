package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarTrackProgress
import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.study_plan.domain.model.StudyPlan

object GamificationToolbarFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val trackProgress: GamificationToolbarTrackProgress?,
            val currentStreak: Int,
            val historicalStreak: HistoricalStreak,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface Message {
        object ClickedStreak : Message
        object ClickedProgress : Message
    }

    internal sealed interface InternalMessage : Message {
        /**
         * Initialization
         */
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object FetchGamificationToolbarDataError : InternalMessage
        data class FetchGamificationToolbarDataSuccess(
            val gamificationToolbarData: GamificationToolbarData
        ) : InternalMessage

        object PullToRefresh : InternalMessage

        /**
         * Flow Messages
         */
        object StepSolved : InternalMessage
        data class StreakChanged(val streak: Streak?) : InternalMessage
        data class StudyPlanChanged(val studyPlan: StudyPlan) : InternalMessage
        object TopicCompleted : InternalMessage
        data class GamificationToolbarDataChanged(
            val gamificationToolbarData: GamificationToolbarData
        ) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowProfileTab : ViewAction
            object ShowProgressScreen : ViewAction
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchGamificationToolbarData(
            val screen: GamificationToolbarScreen,
            val forceUpdate: Boolean
        ) : InternalAction

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}
package org.hyperskill.app.gamification_toolbar.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarTrackProgress
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription

object GamificationToolbarFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val trackProgress: GamificationToolbarTrackProgress?,
            val currentStreak: Int,
            val historicalStreak: HistoricalStreak,
            val subscription: Subscription,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
            internal val isMobileContentTrialEnabled: Boolean,
            internal val canMakePayments: Boolean = false,
            internal val isRefreshing: Boolean = false
        ) : State
    }

    internal val State.isRefreshing: Boolean
        get() = this is State.Content && isRefreshing

    sealed interface ViewState {
        object Idle : ViewState
        object Loading : ViewState
        object Error : ViewState
        data class Content(
            val streak: Streak,
            val progress: Progress?,
            val problemsLimit: ProblemsLimit?
        ) : ViewState {
            data class Progress(
                val value: Float, // a value between 0 and 1
                val formattedValue: String,
                val isCompleted: Boolean
            )

            data class Streak(
                val formattedValue: String,
                val isCompleted: Boolean,
                val isRecovered: Boolean
            )

            data class ProblemsLimit(val limitLabel: String)
        }
    }

    sealed interface Message {
        object ClickedStreak : Message
        object ClickedProgress : Message
        object ClickedSearch : Message
        object ProblemsLimitClicked : Message
    }

    internal sealed interface InternalMessage : Message {
        /**
         * Initialization
         */
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage
        object FetchGamificationToolbarDataError : InternalMessage
        data class FetchGamificationToolbarDataSuccess(
            val gamificationToolbarData: GamificationToolbarData,
            val subscription: Subscription,
            val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
            val isMobileContentTrialEnabled: Boolean,
            val canMakePayments: Boolean
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
        data class SubscriptionChanged(val subscription: Subscription) : InternalMessage
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            object ShowProfileTab : ViewAction
            object ShowProgressScreen : ViewAction
            object ShowSearchScreen : ViewAction
            data class ShowProblemsLimitInfoModal(
                val subscription: Subscription,
                val chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
                val context: ProblemsLimitInfoModalContext
            ) : ViewAction
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
package org.hyperskill.app.profile.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streaks.domain.model.Streak

interface ProfileFeature {
    sealed interface State {
        /**
         * Represents initial state.
         */
        object Idle : State

        /**
         * Represents a state when loading profile screen data.
         */
        object Loading : State

        /**
         * Represents a state when profile screen data successfully loaded.
         *
         * @property profile User profile model.
         * @property streak User profile streak.
         * @property isRefreshing A boolean flag that indicates about is pull-to-refresh is ongoing.
         * @property isLoadingMagicLink A boolean flag that indicates about magic link loading.
         * @see Profile
         * @see Streak
         */
        data class Content(
            val profile: Profile,
            val streak: Streak?,
            val streakFreezeState: StreakFreezeState?,
            val dailyStudyRemindersState: DailyStudyRemindersState,
            val isRefreshing: Boolean = false,
            val isLoadingMagicLink: Boolean = false
        ) : State

        /**
         * Represents a state when profile screen data failed to load.
         */
        object Error : State
    }

    data class DailyStudyRemindersState(
        val isEnabled: Boolean,
        val intervalStartHour: Int
    )

    @Serializable
    sealed interface StreakFreezeState {
        @Serializable
        data class CanBuy(
            val streakFreezeProductId: Long,
            val price: Int
        ) : StreakFreezeState

        @Serializable
        data class NotEnoughGems(
            val streakFreezeProductId: Long,
            val price: Int
        ) : StreakFreezeState

        @Serializable
        object AlreadyHave : StreakFreezeState
    }

    sealed interface Message {
        data class Initialize(
            val isInitCurrent: Boolean = true,
            val profileId: Long? = null,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface ProfileLoaded : Message {
            data class Success(
                val profile: Profile,
                val streak: Streak?,
                val streakFreezeState: StreakFreezeState?,
                val dailyStudyRemindersState: DailyStudyRemindersState
            ) : ProfileLoaded
            object Error : ProfileLoaded
        }

        data class PullToRefresh(
            val isRefreshCurrent: Boolean = true,
            val profileId: Long? = null
        ) : Message

        object StepQuizSolved : Message
        data class HypercoinsBalanceChanged(val hypercoinsBalance: Int) : Message
        data class StreakChanged(val streak: Streak?) : Message

        object ClickedViewFullProfile : Message

        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Streak freeze
         */
        object StreakFreezeCardButtonClicked : Message
        object StreakFreezeModalButtonClicked : Message
        sealed interface StreakFreezeBought : Message {
            object Success : StreakFreezeBought
            object Error : StreakFreezeBought
        }

        data class DailyStudyRemindersIsEnabledClicked(val isEnabled: Boolean) : Message
        data class DailyStudyRemindersIntervalStartHourChanged(val startHour: Int) : Message
        data class DailyStudyRemindersIsEnabledChanged(val isEnabled: Boolean) : Message
        object DailyStudyRemindsTimeClicked : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSettingsEventMessage : Message
        object StreakFreezeModalShownEventMessage : Message
        object StreakFreezeModalHiddenEventMessage : Message
    }

    sealed interface Action {
        data class FetchProfile(val profileId: Long) : Action
        object FetchCurrentProfile : Action

        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : Action

        data class SaveDailyStudyRemindersIsEnabled(val isEnabled: Boolean) : Action
        data class SaveDailyStudyRemindersIntervalStartHour(val startHour: Int) : Action

        data class GetMagicLink(val path: HyperskillUrlPath) : Action

        data class BuyStreakFreeze(
            val streakFreezeProductId: Long,
            val streakFreezePrice: Int
        ) : Action

        sealed interface ViewAction : Action {
            data class OpenUrl(val url: String) : ViewAction
            object ShowGetMagicLinkError : ViewAction

            data class ShowRemindersIntervalDialog(val currentIntervalStartHour: Int) : ViewAction

            data class ShowStreakFreezeModal(val streakFreezeState: StreakFreezeState) : ViewAction
            object HideStreakFreezeModal : ViewAction
            sealed interface ShowStreakFreezeBuyingStatus : ViewAction {
                object Loading : ShowStreakFreezeBuyingStatus
                object Error : ShowStreakFreezeBuyingStatus
                object Success : ShowStreakFreezeBuyingStatus
            }

            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
            }
        }
    }
}
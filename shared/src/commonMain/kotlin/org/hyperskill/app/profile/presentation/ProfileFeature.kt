package org.hyperskill.app.profile.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.view.BadgesViewStateMapper
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
         * @property isLoadingShowed A boolean flag that indicates general loading.
         * @see Profile
         * @see Streak
         */
        data class Content(
            val profile: Profile,
            val streak: Streak?,
            val streakFreezeState: StreakFreezeState?,
            val dailyStudyRemindersState: DailyStudyRemindersState,
            val badgesState: BadgesState,
            val isRefreshing: Boolean = false,
            val isLoadingShowed: Boolean = false
        ) : State

        /**
         * Represents a state when profile screen data failed to load.
         */
        object Error : State
    }

    /**
     * Represents a daily study reminders state.
     *
     * @property isEnabled A boolean flag that indicates about is daily study reminders enabled.
     * This flag doesn't checks if user granted a runtime permission to show notifications.
     *
     * @property startHour A start hour of daily study reminders interval when notifications will be shown.
     */
    data class DailyStudyRemindersState(val isEnabled: Boolean, val startHour: Int)

    /**
     * Represents an available streak freeze states.
     */
    @Serializable
    sealed interface StreakFreezeState {
        /**
         * Represents a state when user can buy a streak freeze.
         *
         * @property streakFreezeProductId A streak freeze product id.
         * @property price A price of streak freeze.
         */
        @Serializable
        data class CanBuy(
            val streakFreezeProductId: Long,
            val price: Int
        ) : StreakFreezeState

        /**
         * Represents a state when user can't buy a streak freeze because of not enough gems.
         *
         * @property streakFreezeProductId A streak freeze product id.
         * @property price A price of streak freeze.
         */
        @Serializable
        data class NotEnoughGems(
            val streakFreezeProductId: Long,
            val price: Int
        ) : StreakFreezeState

        /**
         * Represents a state when user can't buy a streak freeze because of already have one.
         */
        @Serializable
        object AlreadyHave : StreakFreezeState
    }

    /**
     * Represent state of the badges.
     * @property badges represent a list of the badges.
     * Usually it contains all kind of badges.
     * In some cases it may contain only unlocked badges, because of the bug on backend side. See ALT-8526.
     */
    data class BadgesState(val isExpanded: Boolean, val badges: List<Badge>)

    sealed interface Message {
        data class Initialize(
            val isInitCurrent: Boolean = true,
            val profileId: Long? = null,
            val forceUpdate: Boolean = false
        ) : Message

        /**
         * Represents an available profile fetch results.
         *
         * @see Initialize
         * @see Action.FetchProfile
         * @see Action.FetchCurrentProfile
         */
        sealed interface ProfileFetchResult : Message {
            /**
             * Represents a result when profile successfully fetched.
             *
             * @property profile User profile model.
             * @property streak User profile streak.
             * @property streakFreezeState A streak freeze state.
             */
            data class Success(
                val profile: Profile,
                val streak: Streak?,
                val streakFreezeState: StreakFreezeState?,
                val defaultDailyStudyReminderHour: Int,
                val badges: List<Badge>
            ) : ProfileFetchResult

            /**
             * Represents a result when profile failed to fetch.
             */
            object Error : ProfileFetchResult
        }

        data class PullToRefresh(
            val isRefreshCurrent: Boolean = true,
            val profileId: Long? = null
        ) : Message

        object ClickedViewFullProfile : Message

        /**
         * Magic links messages.
         */
        data class GetMagicLinkReceiveSuccess(val url: String) : Message
        object GetMagicLinkReceiveFailure : Message

        /**
         * Streak freeze
         */
        object StreakFreezeCardButtonClicked : Message
        object StreakFreezeModalButtonClicked : Message
        /**
         * Represents an available streak freeze buy results.
         *
         * @see Action.BuyStreakFreeze
         */
        sealed interface BuyStreakFreezeResult : Message {
            object Success : BuyStreakFreezeResult
            object Error : BuyStreakFreezeResult
        }

        /**
         * DailyStudyReminders
         */
        data class DailyStudyRemindersToggleClicked(val isEnabled: Boolean) : Message

        sealed interface DailyStudyRemindersIsEnabledUpdateResult : Message {
            data class Success(val isEnabled: Boolean) : DailyStudyRemindersIsEnabledUpdateResult
            object Error : DailyStudyRemindersIsEnabledUpdateResult
        }

        data class DailyStudyRemindersIntervalStartHourChanged(val startHour: Int) : Message

        sealed interface DailyStudyRemindersIntervalStartHourSaveResult : Message {
            data class Success(val startHour: Int) : DailyStudyRemindersIntervalStartHourSaveResult
            object Error : DailyStudyRemindersIntervalStartHourSaveResult
        }

        data class DailyStudyRemindersIsEnabledChanged(val isEnabled: Boolean) : Message

        /**
         * Badges
         */
        data class BadgesVisibilityButtonClicked(val visibilityButton: BadgesVisibilityButton) : Message
        enum class BadgesVisibilityButton {
            SHOW_ALL,
            SHOW_LESS
        }
        data class BadgeClicked(val badgeKind: BadgeKind) : Message
        data class BadgeModalShownEventMessage(val badgeKind: BadgeKind) : Message
        data class BadgeModalHiddenEventMessage(val badgeKind: BadgeKind) : Message

        /**
         * Flow messages.
         */
        object StepQuizSolved : Message
        data class ProfileChanged(
            val profile: Profile,
            val defaultDailyStudyReminderHour: Int
        ) : Message
        data class StreakChanged(val streak: Streak?) : Message

        /**
         * Analytic
         */
        object ViewedEventMessage : Message
        object ClickedSettingsEventMessage : Message
        object ClickedDailyStudyRemindsTimeEventMessage : Message
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

            sealed interface ShowError : ViewAction {
                object MagicLink : ShowError

                object DailyStudyReminders : ShowError
            }

            data class ShowStreakFreezeModal(val streakFreezeState: StreakFreezeState) : ViewAction
            object HideStreakFreezeModal : ViewAction
            sealed interface ShowStreakFreezeBuyingStatus : ViewAction {
                object Loading : ShowStreakFreezeBuyingStatus
                object Error : ShowStreakFreezeBuyingStatus
                object Success : ShowStreakFreezeBuyingStatus
            }

            data class ShowBadgeDetailsModal(val details: BadgeDetails) : ViewAction

            /**
             * Represents a data to show the badge detailed modal.
             */
            @Serializable
            sealed interface BadgeDetails {
                val badgeKind: org.hyperskill.app.badges.domain.model.BadgeKind

                /**
                 * Represents a data to show the badge detailed modal
                 * in case these badge kind data is returned by backend.
                 */
                @Serializable
                data class FullBadge(val badge: Badge) : BadgeDetails {
                    override val badgeKind: org.hyperskill.app.badges.domain.model.BadgeKind
                        get() = badge.kind
                }

                /**
                 * Represents a data to show the badge detailed modal
                 * in case the backend didn't return these badge kind data.
                 * @see [BadgesViewStateMapper], [BadgesState]
                 */
                @Serializable
                data class BadgeKind(
                    override val badgeKind: org.hyperskill.app.badges.domain.model.BadgeKind
                ) : BadgeDetails
            }

            sealed interface NavigateTo : ViewAction {
                object HomeScreen : NavigateTo
            }
        }
    }
}